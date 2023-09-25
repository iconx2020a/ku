package secretapp;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;	
public class SecretManager {
     String dbPassword = "dbpassword";
     String dbUserName = "dbusername";
     String dbURL = "dburl";
    Region region = Region.of("us-east-1");
    GetSecretValueResponse getSecretValueResponse;
    SecretsManagerClient client;
     GetSecretValueRequest getSecretValueRequest;
    public SecretManager() {
         client = SecretsManagerClient.builder()
            .region(region)
            .build();
          try {
        readFile();
    } catch (Exception e) {
        
        throw e;
    }
  
    }

    private void readFile() {
             getSecretValueRequest = GetSecretValueRequest.builder()
            .secretId(dbURL)
            .build();
             getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
             String secret = getSecretValueResponse.secretString();
             System.out.println(secret);
           
             getSecretValueRequest = GetSecretValueRequest.builder()
            .secretId(dbUserName)
            .build();
             getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
             secret = getSecretValueResponse.secretString();
             System.out.println(secret);
        
            getSecretValueRequest = GetSecretValueRequest.builder()
            .secretId(dbPassword)
            .build();
             getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
             secret = getSecretValueResponse.secretString();
             System.out.println(secret);
    }
    public static void main (String [ ] args){
      new SecretManager();
   
    }
}
