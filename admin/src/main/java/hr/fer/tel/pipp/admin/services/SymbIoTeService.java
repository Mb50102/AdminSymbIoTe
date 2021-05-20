package hr.fer.tel.pipp.admin.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.h2020.symbiote.security.ClientSecurityHandlerFactory;
import eu.h2020.symbiote.security.commons.Certificate;
import eu.h2020.symbiote.security.commons.Token;
import eu.h2020.symbiote.security.commons.credentials.AuthorizationCredentials;
import eu.h2020.symbiote.security.commons.credentials.BoundCredentials;
import eu.h2020.symbiote.security.commons.enums.AccountStatus;
import eu.h2020.symbiote.security.commons.enums.ManagementStatus;
import eu.h2020.symbiote.security.commons.enums.OperationType;
import eu.h2020.symbiote.security.commons.enums.UserRole;
import eu.h2020.symbiote.security.commons.exceptions.custom.AAMException;
import eu.h2020.symbiote.security.commons.exceptions.custom.SecurityHandlerException;
import eu.h2020.symbiote.security.commons.exceptions.custom.ValidationException;
import eu.h2020.symbiote.security.communication.AAMClient;
import eu.h2020.symbiote.security.communication.IAAMClient;
import eu.h2020.symbiote.security.communication.payloads.*;
import eu.h2020.symbiote.security.handler.ISecurityHandler;
import eu.h2020.symbiote.security.handler.SecurityHandler;
import eu.h2020.symbiote.security.helpers.MutualAuthenticationHelper;
import hr.fer.tel.pipp.admin.model.Resource;
import hr.fer.tel.pipp.admin.model.Room;
import hr.fer.tel.pipp.admin.model.User;
import hr.fer.tel.pipp.admin.model.UserRequest;
import hr.fer.tel.pipp.admin.repositories.UserRepository;
import hr.fer.tel.pipp.admin.repositories.UserRequestRepository;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.SecondaryTable;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class SymbIoTeService {

    private static Log log = LogFactory.getLog(SymbIoTeService.class);

    @Autowired
    private UserRequestRepository userRequestRepository;

    @Autowired
    private UserRepository userRepository;

    private String symbIoTeCoreUrl;
    private String coreAAMAddress;
    private String clientId;
    private String userId;
    private String paamOwnerUsername;
    private String paamOwnerPassword;
    private String platformId;
    private String aamUrl;
    private String keystorePath;
    private String keystorePassword;
    private ISecurityHandler securityHandler;
    private Credentials paamOwnerCredentials;

    @Autowired
    public SymbIoTeService(@Value("${symbioteCoreUrl}") String symbIoTeCoreUrl, @Value("${coreAAMAddress}") String coreAAMAddress,
                           @Value("${keystorePath}") String keystorePath, @Value("${keystorePassword}") String keystorePassword,
                           @Value("${userId}") String userId, @Value("${clientId}") String clientId,
                           @Value("${paamOwner.username}") String paamOwnerUsername, @Value("${paamOwner.password}") String paamOwnerPassword,
                           @Value("${platformId}") String platformId, @Value("${aamUrl}") String aamUrl) throws SecurityHandlerException {

        Assert.notNull(symbIoTeCoreUrl,"symbIoTeCoreUrl can not be null!");
        Assert.notNull(coreAAMAddress,"coreAAMAddress can not be null!");
        Assert.notNull(keystorePath,"keystorePath can not be null!");
        Assert.notNull(keystorePassword,"keystorePassword can not be null!");
        Assert.notNull(userId,"userId can not be null!");
        Assert.notNull(clientId,"clientId can not be null!");
        Assert.notNull(paamOwnerUsername,"paamOwnerUsername can not be null!");
        Assert.notNull(paamOwnerPassword,"paamOwnerPassword can not be null!");
        Assert.notNull(platformId, "platformId cannot be null");
        Assert.notNull(aamUrl, "aamUrl can not be null!");

        this.symbIoTeCoreUrl = symbIoTeCoreUrl;
        this.coreAAMAddress = coreAAMAddress;
        this.keystorePath = keystorePath;
        this.keystorePassword = keystorePassword;
        this.userId = userId;
        this.clientId = clientId;
        this.paamOwnerUsername = paamOwnerUsername;
        this.paamOwnerPassword = paamOwnerPassword;
        this.platformId = platformId;
        this.aamUrl = aamUrl;

        securityHandler = ClientSecurityHandlerFactory.getSecurityHandler(coreAAMAddress, keystorePath,
                keystorePassword, userId);
        paamOwnerCredentials = new Credentials(paamOwnerUsername, paamOwnerPassword);
    }

    public ManagementStatus registerUser(UserRequest userRequest) throws AAMException, SecurityHandlerException {

        log.info("Registering to PAAM: " + platformId);
        Map<String, AAM> availableAAMs = securityHandler.getAvailableAAMs();
        String aamAddress = availableAAMs.get(platformId).getAamAddress();
        IAAMClient aamClient = new AAMClient(aamAddress);
        log.info("Registering to PAAM: " + platformId + " with url " + aamAddress);

        Credentials userCredentials = new Credentials(userRequest.getUsername(), userRequest.getPassword());
        UserDetails userDetails = new UserDetails();
        userDetails.setCredentials(userCredentials);
        userDetails.setRole(UserRole.USER);
        userDetails.setRecoveryMail(userRequest.getEmail());
        userDetails.setAttributes(buildAttributes(userRequest.getRooms()));
        userDetails.setStatus(AccountStatus.ACTIVE);
        userDetails.setServiceConsent(true);
        userDetails.setAnalyticsAndResearchConsent(true);

        UserManagementRequest userManagementRequest = new UserManagementRequest(
                paamOwnerCredentials,
                userCredentials, userDetails,
                OperationType.CREATE
        );

        ManagementStatus status = aamClient.manageUser(userManagementRequest);
        log.debug(status);

        Map<String, BoundCredentials> ac = securityHandler.getAcquiredCredentials();
        for (String s : ac.keySet()) {
            System.out.println(s + ": " + ac.get(s));
        }

        return status;
    }

    public ManagementStatus deleteUser(User user) throws AAMException, SecurityHandlerException {
        log.info("Deleting from PAAM: " + platformId);
        Map<String, AAM> availableAAMs = securityHandler.getAvailableAAMs();
        String aamAddress = availableAAMs.get(platformId).getAamAddress();
        IAAMClient aamClient = new AAMClient(aamAddress);
        log.info("Deleting from PAAM: " + platformId + " with url " + aamAddress);

        // TODO kaj stavit u credentials za password?
        Credentials userCredentials = new Credentials(user.getUsername(), "");
        UserDetails userDetails = new UserDetails();
        userDetails.setCredentials(userCredentials);
        userDetails.setRole(UserRole.USER);
        userDetails.setRecoveryMail(user.getEmail());
        userDetails.setAttributes(buildAttributes(user.getRooms()));
        userDetails.setStatus(AccountStatus.ACTIVE);
        userDetails.setServiceConsent(true);
        userDetails.setAnalyticsAndResearchConsent(true);

        UserManagementRequest userManagementRequest = new UserManagementRequest(
                paamOwnerCredentials,
                userCredentials, userDetails,
                OperationType.DELETE
        );

        ManagementStatus status = aamClient.manageUser(userManagementRequest);
        log.debug(status);

        return status;
    }

    public ManagementStatus updateUser(User oldUser, User newUser) throws AAMException, SecurityHandlerException {

        ManagementStatus status = deleteUser(oldUser);
        if (status != ManagementStatus.OK) return status;

        UserRequest newUserRequest = new UserRequest();
        newUserRequest.setEmail(newUser.getEmail());
        newUserRequest.setUsername(newUser.getUsername());
        newUserRequest.setPassword(newUser.getPassword());
        newUserRequest.setRooms(newUser.getRooms());
        return registerUser(newUserRequest);
    }

    private Map<String, String> buildAttributes(List<Room> rooms) {

        Map<String, String> attributes = new HashMap<>();

        for (Room r : rooms) {
            attributes.put(r.getRoomName(), "true");
        }

        return attributes;
    }
    public void HomeToken() throws SecurityHandlerException, ValidationException, NoSuchAlgorithmException, JsonProcessingException {
        Map<String, AAM> availableAAMs = securityHandler.getAvailableAAMs();
        AAM aam = availableAAMs.get(platformId);
        Certificate drugi = securityHandler.getCertificate(aam, "drugi", "12345", "administration_backend");
        Set<AuthorizationCredentials> authorizationCredentialsSet = new HashSet<>();
        Token token = securityHandler.login(aam);
        System.out.println(token.getType()+": "+token);
        authorizationCredentialsSet.add(new AuthorizationCredentials(token, aam, securityHandler.getAcquiredCredentials().get(aam.getAamInstanceId()).homeCredentials));


        SecurityRequest securityRequest = MutualAuthenticationHelper.getSecurityRequest(authorizationCredentialsSet, false);
        Map<String, String> securityHeaders = securityRequest.getSecurityRequestHeaderParams();
        System.out.println(securityRequest.toString());
        System.out.println("Heeej");
        securityHeaders.forEach((String,string)->System.out.println(String+" : "+string));
    }



}
