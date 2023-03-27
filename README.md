# COMP231-002Team6Project-iStockMasterPro

Our team recommends using the Eclipse IDE to run the application. Download Eclipse from this link: https://www.eclipse.org/downloads/


To download the project, follow one of these methods:

Method 1:
1. Click "Code"
2. Select "Download ZIP"
3. Unzip the folder

Method 2:
1. Create a new folder on your device
2. Right-click inside the folder and choose "Git Bash Here" (download Git from https://git-scm.com/downloads if needed)
3. Enter the following command: git clone https://github.com/kwwong0923/COMP231-002Team6Project-iStockMasterPro.git


After launching Eclipse IDE:

1. Choose a directory for your workspace and click "Launch"
2. Go to File > Import > Existing Projects into Workspace
3. For "Select root directory," choose the downloaded GitHub folder
4. Click "Finish"
5. In the Package Explorer, right-click the folder and choose Build Path > Configure Build Path…
6. Select "Libraries" and click "Add External JARs…"
7. Add the following files:

    i. All JAR files in …\COMP231-002Team6Project-iStockMasterPro-master\javafx-sdk-19.0.2.1\lib      
    
    ii. ojdbc11.jar
    
    iii. (You may also need to remove any old error paths or JARs)
8. Click "Apply and Close"
9. In the Package Explorer, navigate to src > application > Main.java
10. Click the "Run" button to launch the application
