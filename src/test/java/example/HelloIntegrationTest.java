package example;

import com.google.gson.Gson;
import org.junit.Assert;
import org.apache.http.entity.ContentType;
import org.junit.Test;

public class HelloIntegrationTest
{

    String LAMBDA_URL = "https://tph3djc4zl.execute-api.us-west-2.amazonaws.com/test/sayHello";

    @Test
    public void testSayHello() throws Exception
    {
        Request request = new Request("Tim", "Burton");
        Gson gson = new Gson();
        String requestStr = gson.toJson(request);

        String responseStr = org.apache.http.client.fluent.Request.Post(LAMBDA_URL)
            .bodyString(requestStr, ContentType.APPLICATION_JSON)
            .execute()
            .returnContent()
            .asString();

        Response response = gson.fromJson(responseStr, Response.class);
        Assert.assertEquals("Hello "+request.getFirstName() +" "+ request.getLastName() + ".",
            response.getGreetings());

    }


}