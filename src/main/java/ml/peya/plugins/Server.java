package ml.peya.plugins;

import com.fasterxml.jackson.databind.*;
import org.msgpack.jackson.dataformat.*;

import java.io.*;
import java.net.*;

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

    public JsonNode quickAccess(String func, String method, String body, boolean msgpack)
    {
        HttpURLConnection connection = null;
        try
        {
            String url = addr + func;
            if (!msgpack)
                body += "&raw=true";
            if (method.equals("GET"))
                url += "?" + body;
            connection = (HttpURLConnection) new URL("http://" + url).openConnection();
            connection.setConnectTimeout(200);
            connection.setRequestMethod(method);
            connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            if (!token.equals(""))
                connection.addRequestProperty("Token", token);
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

            byte[] result = streamToBytes(connection.getInputStream());

            if (msgpack)
                return new ObjectMapper(new MessagePackFactory()).readValue(result, JsonNode.class);
            else
                return new ObjectMapper().readTree(result);
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                try
                {
                    byte[] result = streamToBytes(connection.getErrorStream());

                    if (msgpack)
                        return new ObjectMapper(new MessagePackFactory()).readValue(result, JsonNode.class);
                    else
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

    private byte[] streamToBytes(InputStream stream)
    {
        try
        {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[8192];
            while ((nRead = stream.read(data, 0, data.length)) != -1)
                buffer.write(data, 0, nRead);
            return buffer.toByteArray();
        }
        catch (Exception e)
        {
            return new byte[0];
        }
    }
}
