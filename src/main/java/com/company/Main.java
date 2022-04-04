package com.company;

import com.company.Extensions.ConsoleColors;
import com.company.WebClient.WebClient;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {

        args = new String[] {"localhost", "50070", "petya"};

        WebClient webClient = new WebClient(args[2], args[0], args[1]);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String command = "";

        System.out.println();
        System.out.println(ConsoleColors.MakePurple("Введите 'exit', чтобы выйти"));
        System.out.println();

        while(!command.equals("exit")){
            System.out.print("[" + webClient.GetCurrentPath() + "]" + " " + "-> ");
            command = reader.readLine();
            webClient.InputCommand(command);
        }


        /* FileSystem API
        InputStream in = null;
        try {
            in = new URL("hdfs://host/path").openStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(in);
        }

        FileSystem fs = new FileSystem() {
            @Override
            public URI getUri() {
                return null;
            }

            @Override
            public FSDataInputStream open(Path path, int i) throws IOException {
                return null;
            }

            @Override
            public FSDataOutputStream create(Path path, FsPermission fsPermission, boolean b, int i, short i1, long l, Progressable progressable) throws IOException {
                return null;
            }

            @Override
            public FSDataOutputStream append(Path path, int i, Progressable progressable) throws IOException {
                return null;
            }

            @Override
            public boolean rename(Path path, Path path1) throws IOException {
                return false;
            }

            @Override
            public boolean delete(Path path, boolean b) throws IOException {
                return false;
            }

            @Override
            public FileStatus[] listStatus(Path path) throws FileNotFoundException, IOException {
                return new FileStatus[0];
            }

            @Override
            public void setWorkingDirectory(Path path) {

            }

            @Override
            public Path getWorkingDirectory() {
                return null;
            }

            @Override
            public boolean mkdirs(Path path, FsPermission fsPermission) throws IOException {
                return false;
            }

            @Override
            public FileStatus getFileStatus(Path path) throws IOException {
                return null;
            }
        };
        */
	// write your code here
    }


}
