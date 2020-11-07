package ml.peya.plugins;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Server
{
    String addr;
    String token;

    public Server(String addr, String token)
    {
        this.addr = addr;
        this.token = token;
    }

    public boolean pingTest()
    {
        HttpURLConnection connection = null;
        boolean success = false;
        try
        {
            connection = (HttpURLConnection) new URL("http://" + addr + "/teapot").openConnection();
            connection.setConnectTimeout(599);
            connection.setRequestMethod("GET");
            connection.connect();
            success = true;
        }
        catch (SocketTimeoutException e)
        {
            return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (connection != null)
                connection.disconnect();
        }
        return success;
    }

    public JsonNode quickAccess(String func, String method, String body)
    {
        HttpURLConnection connection = null;
        try
        {
            String url = addr + func;
            if (method.equals("GET"))
                url = url + "?" + body;
            connection = (HttpURLConnection) new URL("http://" + url).openConnection();
            connection.setConnectTimeout(200);
            connection.setRequestMethod(method);
            connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            if (!PeyangGreatBanManager.config.getString("server.token").equals(""))
                connection.addRequestProperty("Token", PeyangGreatBanManager.config.getString("server.token"));
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.connect();

            if (!method.equals("GET"))
            {
                try (PrintStream stream = new PrintStream(connection.getOutputStream()))
                {
                    stream.print(body);
                }
            }

            String result = streamToString(connection.getInputStream());

            return new ObjectMapper().readTree(result);


        }
        catch (Exception e)
        {
            if (connection != null)
            {
                try
                {
                    String result = streamToString(connection.getErrorStream());

                    return new ObjectMapper().readTree(result);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        finally
        {
            if (connection != null)
                connection.disconnect();
        }
        return null;
    }

    private String streamToString(InputStream stream)
    {
        try
        {
            StringBuilder sb = new StringBuilder();
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            while ((line = br.readLine()) != null)
                sb.append(line);

            stream.close();
            return sb.toString();
        }
        catch (Exception e)
        {
            return "{\"success\":false,\"cause\":\"Failed to parsing result.\"}";
        }
    }
}
