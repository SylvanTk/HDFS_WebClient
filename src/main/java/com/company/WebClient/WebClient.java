package com.company.WebClient;

import com.company.Enums.OperationType;
import com.company.Enums.RequestMethod;
import com.company.JSONHandler.JSONHandler;
import com.company.Extensions.ConsoleColors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class WebClient {
    private final String UserName;  
    private final String Host;
    private final String Port;
    private OperationType Operation;
    private String CurrentPath = "/user/petya/mydata/HITS";
    private String RemotePath = "";
    private String LocalPath = "";


    public WebClient(String UserName, String Host, String Port){
        this.UserName = UserName;
        this.Host = Host;
        this.Port = Port;
    }

    public void InputCommand(String expression){
        String[] splittedExpression = expression.split(" ");
        String command = splittedExpression[0];

        if(splittedExpression.length > 1)
            RemotePath = splittedExpression[1];
        if(splittedExpression.length > 2){
            LocalPath = splittedExpression[1];
            RemotePath = splittedExpression[2];
        }

        ClassifyOperation(command);
    }

    private void ClassifyOperation(String command){
        switch(command){
            case "ls":
                ListDirectory(true);
                break;
            case "mkdir":
                MakeDirectory();
                break;
            case "put":
                CreateFile();
                break;
            case "get":
                GetFile();
                break;
            case "append":
                AppendFile();
                break;
            case "delete":
                DeleteFile();
                break;
            case "cd":
                ChangeDirectory();
                break;
            case "exit":
                System.out.println(ConsoleColors.MakePurple("Пока!"));
                break;
            case "lls":
            case "lcd":
            default:
                System.out.println(ConsoleColors.MakeRed("Не является командой"));
                break;
        }
    }

    private String MakeConnectionString(boolean includeRemotePath){
        StringBuilder connectionString;

        if(includeRemotePath)
            connectionString = new StringBuilder("http://" + Host + ":" + Port + "/webhdfs/v1" + CurrentPath + (RemotePath.isEmpty() ? "" : "/" + RemotePath) + "?" + "user.name=" + UserName + "&" + "op=" + Operation.toString());
        else
            connectionString = new StringBuilder("http://" + Host + ":" + Port + "/webhdfs/v1" + CurrentPath + "?" + "user.name=" + UserName + "&" + "op=" + Operation.toString());

        return connectionString.toString();
    }

    private String MakeConnectionString(boolean includeRemotePath, boolean recursive){
        StringBuilder connectionString;

        if(includeRemotePath)
            connectionString = new StringBuilder("http://" + Host + ":" + Port + "/webhdfs/v1" + CurrentPath + (RemotePath.isEmpty() ? "" : "/" + RemotePath) + "?" + "user.name=" + UserName + "&" + "op=" + Operation.toString() + (recursive ? "&" + "recursive=true" : ""));
        else
            connectionString = new StringBuilder("http://" + Host + ":" + Port + "/webhdfs/v1" + CurrentPath + "?" + "user.name=" + UserName + "&" + "op=" + Operation.toString());

        return connectionString.toString();
    }

    private HttpURLConnection MakeConnection(String connectionString){
        HttpURLConnection connection = null;

        try{
            URL url = new URL(connectionString);
            connection = (HttpURLConnection) url.openConnection();
        } catch(IOException urlException){
            System.out.println(urlException.getMessage());
        }

        return connection;
    }

    private void ListDirectory(boolean print) {
        this.Operation = OperationType.LISTSTATUS;
        HttpURLConnection connection = MakeConnection(MakeConnectionString(false));

        try {
            connection.setRequestMethod(RequestMethod.GET.name());
        } catch (ProtocolException protocolException) {
            System.out.println(protocolException.getMessage());
        }

        connection.setDoOutput(false);
        connection.setDoInput(true);


        try {
            BufferedReader response = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder json = new StringBuilder();
            while (response.ready())
                json.append(response.readLine());

            JSONHandler jsonHandler = new JSONHandler(json.toString());
            jsonHandler.DeserializeFileStatuses();

            if(print)
                jsonHandler.PrintDirs();

        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }
    }

    private void MakeDirectory(){
        this.Operation = OperationType.MKDIRS;
        HttpURLConnection connection = MakeConnection(MakeConnectionString(true));

        try{
            connection.setRequestMethod(RequestMethod.PUT.name());
        }
        catch(ProtocolException protocolException){
            System.out.println(protocolException.getMessage());
        }

        connection.setDoInput(true);


        try{
            BufferedReader response = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder json = new StringBuilder();
            while(response.ready())
                json.append(response.readLine());

            JSONHandler jsonHandler = new JSONHandler(json.toString());
            jsonHandler.DeserializeBoolean().PrintBoolean();
        }
        catch(IOException ioException){
            System.out.println(ioException.getMessage());
        }

        RemotePath = "";
    }

    private void DeleteFile(){
        ListDirectory(false);

        JSONHandler jsonHandler = new JSONHandler();

        if(!jsonHandler.objectExists(RemotePath))
            System.out.println(ConsoleColors.MakeRed("Файла не существует в текущей директории"));
        else{
            this.Operation = OperationType.DELETE;

            String connectionString;

            if(!LocalPath.isEmpty() && LocalPath.equals("-r"))
                connectionString = MakeConnectionString(true, true);
            else
                connectionString = MakeConnectionString(true);

            HttpURLConnection connection = MakeConnection(connectionString);

            try{
                connection.setRequestMethod(RequestMethod.DELETE.name());
            }
            catch(ProtocolException protocolException){
                System.out.println(protocolException.getMessage());
            }

            connection.setDoInput(true);

            try{
                BufferedReader response = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder json = new StringBuilder();
                while(response.ready())
                    json.append(response.readLine());

                jsonHandler.InputJSON(json.toString());
                jsonHandler.DeserializeBoolean().PrintBoolean();
            }
            catch(IOException ioException) {
                System.out.println(ioException.getMessage());
            }
        }

        ClearPaths();
    }

    private void ChangeDirectory(){
        ListDirectory(false);

        if(RemotePath.equals(""))
            System.out.println(ConsoleColors.MakeRed("Пустой аргумент!"));
        else{
            if(RemotePath.equals("..")){
                if(CurrentPath.equals("/"))
                    System.out.println(ConsoleColors.MakeRed("Невозможно переместиться выше корневой директории"));
                else
                    if(CurrentPath.split("/").length == 2)
                        CurrentPath = "/";
                    else
                        CurrentPath = CurrentPath.substring(0, CurrentPath.lastIndexOf("/"));
            }
            else{
                JSONHandler jsonHandler = new JSONHandler();

                if(jsonHandler.directoryExists(RemotePath))
                    if(CurrentPath.equals("/"))
                        CurrentPath = "/" + RemotePath;
                    else
                        CurrentPath = CurrentPath + "/" + RemotePath;
                else
                    System.out.println(ConsoleColors.MakeRed("Такой директории не существует!"));
            }
        }
    }

    public String GetCurrentPath(){
        return this.CurrentPath;
    }


    private void CreateFile(){
        this.Operation = OperationType.CREATE;

        String path = RemotePath;
        String[] filePath = RemotePath.split("/");

        RemotePath = filePath[filePath.length - 1].split("\\.")[0];

        HttpURLConnection connection = MakeConnection(MakeConnectionString(true));

        try{
            connection.setRequestMethod(RequestMethod.PUT.name());
        }
        catch(ProtocolException protocolException){
            System.out.println(protocolException.getMessage());
        }

        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(true);

        try{
            File file = new File(path);
            IOUtils.copy(FileUtils.openInputStream(file), connection.getOutputStream());

            Map<String, List<String>> headers = connection.getHeaderFields();
            for(String header : headers.keySet())
                System.out.println(header + ": " + headers.get(header));

        }
        catch(IOException ioException) {
            System.out.println(ioException.getMessage());
        }

        ClearPaths();
    }

    private void GetFile(){
        ListDirectory(false);
        JSONHandler jsonHandler = new JSONHandler();

        if(!jsonHandler.fileExists(RemotePath))
            System.out.println(ConsoleColors.MakeRed("Файл не найден"));
        else{
            this.Operation = OperationType.OPEN;
            HttpURLConnection connection = MakeConnection(MakeConnectionString(true));

            try{
                connection.setRequestMethod(RequestMethod.GET.name());
            } catch(ProtocolException protocolException){
                System.out.println(protocolException.getMessage());
            }

            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(true);

            try{
                InputStream inputStream = connection.getInputStream();
                int ch;
                while((ch = inputStream.read()) != -1)
                    System.out.print((char)ch);
            } catch(IOException ioException) {
                System.out.println(ioException.getMessage());
            }
            connection.disconnect();
        }

    }

    private void AppendFile(){
        ListDirectory(false);
        JSONHandler jsonHandler = new JSONHandler();
        String[] files = jsonHandler.GetFiles();

        boolean fileExists = false;
        for(String file : files)
            if(fileExists)
                break;
            else
                if(file.equals(RemotePath))
                    fileExists = true;

        if(!fileExists)
            System.out.println(ConsoleColors.MakeRed("Файл не найден"));
        else{
            this.Operation = OperationType.APPEND;
            HttpURLConnection connection = MakeConnection(MakeConnectionString(true));
            connection.setDoOutput(true);
            connection.setDoInput(true);
            try{
                connection.setRequestMethod(RequestMethod.POST.name());
            } catch(ProtocolException protocolException){
                System.out.println(protocolException.getMessage());
            }

            try{
                File file = new File(LocalPath);
                IOUtils.copy(FileUtils.openInputStream(file), connection.getOutputStream());

                Map<String, List<String>> headers = connection.getHeaderFields();
                for(String header : headers.keySet())
                    System.out.println(header + ": " + headers.get(header));

            } catch(IOException ioException) {
                System.out.println(ioException.getMessage());
            }
        }
    }

    private void ClearPaths(){
        this.RemotePath = "";
        this.LocalPath = "";
    }

}
