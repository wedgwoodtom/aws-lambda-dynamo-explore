package example;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.Context;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

/**
 *
 */
public class Hello implements RequestHandler<Request, Response>
{
    private DynamoDB dynamoDb;
    private String DYNAMODB_TABLE_NAME = "People2";
    private Regions REGION = Regions.US_WEST_2;

    public Response handleRequest(Request request, Context context)
    {
        String contextToStr = ToStringBuilder.reflectionToString(context);
        context.getLogger().log("Context="+contextToStr);

        initDynamoDbClient();
        save(request);

        String greetingString = String.format("Hello %s %s.", request.firstName, request.lastName);
        return new Response(greetingString);
    }

    private void initDynamoDbClient()
    {
        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(REGION));
        dynamoDb = new DynamoDB(client);
    }

    private void save(Request request)
    {
        // TODO: Can do this mapping with DynamoDBMapper instead - yeah!
        DynamoDBMapper mapper;

        dynamoDb.getTable(DYNAMODB_TABLE_NAME)
            .putItem(
                new PutItemSpec().withItem(new Item()
                    .withString("id", UUID.randomUUID().toString())
                    .withString("firstName", request.getFirstName())
                    .withString("lastName", request.getLastName())
                )
            );
    }

}
