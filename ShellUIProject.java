import java.io.IOException; // Needed for handleing errors 

// Needed to show the UI via JavaFX 
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage; 

public class ShellUIProject extends Application { //Class ShellUIProject entaend the Application super class to inhernt the JavaFX 
    // varibles used inside Start() and others 
    private TextArea outputArea; // to show the output 
    private ComboBox<String> commandBox; // to show the command 

    @Override
    public void start(Stage stage) { // otou call then lunch() at the main() start 
        // the name of the window 
        stage.setTitle("Shell Command UI");

        commandBox = new ComboBox<>(); // menu to show all the command 
        commandBox.getItems().addAll( // Fill the menu with command 
    "echo hello",                                          //print hello for the user 
    "whoami",                                               //print the current system
    "echo. > file.txt",                                     // create a new file 
    "echo hello > file.txt",                               //wite inside file.txt
    "find \"hello\" file.txt",                             //search for hello inside file.txt
    "more file.txt",                                       // show large text page by page 
    "type file.txt",                                       // list content of file.txt     
    "move file.txt moved.txt",                             //  copey context of file.txt into moved.txt 
    "type moved.txt",                                       // list content of moved.txt
    "del moved.txt",                                       // remove moved.txt 
    "del file.txt",                                       // remove file.txt 
    "mkdir testDir",                                       // make directory
    "rmdir /s /q testDir",                                  // remove directory by force
    "cd..",                                                 //change the current path 
    "cd",                                                  //print current directory 
    "dir",                                                 // list all files
    "powershell -Command \"Get-Content file.txt -TotalCount 5\"",  // show the fisrt 5 lines of the file 
    "icacls file.txt",                                     // show premision of files 
    "icacls file.txt /grant %USERNAME%:F",                // change a premision of file to full accsess   
    "dir /s",                                              // show the subdirectories and the disk usge
    "dir /s /b *.txt"  ,                                // show all the files with .txt inside the directory   
    //--------------------------------------------------------------------------------------------------------
    //for the next 4 command , we need to set the program as Administrator to run it 
    "net user Taghreed 123 /add",                                // adduser Taghreed
    "net localgroup devs /add",                             // addgroup devs
    "icacls file.txt /setowner Taghreed" ,                       // chown Taghreed file.txt
    //--------------------------------------------------------------------------------------------------------   
     "tasklist",                                         //show the processes implementing on the OS  
     "fsutil quota", // check the usge of disk quotas for a user                                       //
     "dir /s /b file.txt"    //search for the location of the file 
                
      //man, wc, get,ile, chgrp, chown, adduser,gzip, addgroub is unseporrted in windows os 
                
        );
        commandBox.setPromptText("Choose a command"); // text on the bar "choose a command " for the user to choose 

        Button runButton = new Button("Run Command"); // create a bottom the will run the command 
        runButton.setOnAction(e -> runCommand());

        outputArea = new TextArea(); // show the result of the command in the textArea 
        outputArea.setEditable(false);// user cant write here 
        outputArea.setWrapText(true); // text wont go out of the screan 

        // For the desine of the UI
        // List the command in a paralle shape with 10 shape apart 
        VBox layout = new VBox(10, commandBox, runButton, outputArea);
        layout.setStyle("-fx-padding: 20"); // a speace between each command 

        // for the desin of the window 
        Scene scene = new Scene(layout, 500, 400); // set window w=500 , h=400
        stage.setScene(scene); // link the seane with the window 
        stage.show(); // Finally show the windwo 
    }

    public static void main(String[] args) {
        launch(); // when launch() start , start() will execute
    }

    // a method when the user choose a command and run the command 
    private void runCommand() {
    String command = commandBox.getValue(); // get the value of the command 
    // if the  commandBox.getValue() is null or emty , show a warning massge for the user 
    if (command == null || command.isEmpty()) {
        outputArea.setText("Please select a command.");
        return;
    }
    
    //Try and catch for handling errors and commands 
    try {
        ProcessBuilder builder;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            builder = new ProcessBuilder("cmd.exe", "/c", command); // check if windows system (currtnly use in our project)
        } else {
            builder = new ProcessBuilder("bash", "-c", command);// check of mac oe linux system 
        }

        Process process = builder.start();// Start executing 

        // Read output
        java.io.InputStream is = process.getInputStream(); // input stream 
        java.io.InputStream es = process.getErrorStream(); // error stream

        java.util.Scanner outScanner = new java.util.Scanner(is).useDelimiter("\\A"); // read all data from is 
        java.util.Scanner errScanner = new java.util.Scanner(es).useDelimiter("\\A");// read all data from er

        String output = outScanner.hasNext() ? outScanner.next() : ""; // if theres data then save it inside output
        String error = errScanner.hasNext() ? errScanner.next() : "";// if theres erros then save it inside error

        if (!error.isEmpty()) {
            outputArea.setText("Error:\n" + error); // show the errors if exist
        } else {
            outputArea.setText(output); // if not then show the output 
        }

    } catch (IOException ex) { // catch for general errors while running 
        outputArea.setText("Exception: " + ex.getMessage());
    }
  }
}
