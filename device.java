import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;  
import javax.swing.Timer; 
import java.security.*;





class device_Database
{
    int pn,pos;
    Socket socket;
    DataInputStream in;
    DataOutputStream out;
    String name,ip;
    String buffer;
    String terminal_buffer;
    String terminal_log;
    String computing_log;
    String message_log;
    int file_transfer_flag;

    device_Database()
    {
        pn=-1;
        pos=-1;
        socket=null;
        in=null;
        out=null;
        name="";
        ip="";
        buffer="";
        message_log="";
        terminal_log="";
        terminal_buffer="";
        file_transfer_flag=0;
    }

    @Override
    public String toString()
    {
        return name;
    }
}



public class device {

    public static String computing_terminal;
    public static int computing_flag;
    private static final Semaphore mutex=new Semaphore(1);
    private static final Semaphore rwmutex=new Semaphore(1);
    private static int load=0;
    private static int takeaway=10;
    private static int amount_computed=0;

    
    static public class matrix_addition
    {

        public static int xf=100;
        public static int yf=10; 

        public static String add(String s)
        {
            String file = "C:\\Users\\adity\\OneDrive\\Desktop\\All Folders\\college work vit new\\Networks Project\\Multiclient\\Main Project\\matrix1.csv"; 
            String file2 = "C:\\Users\\adity\\OneDrive\\Desktop\\All Folders\\college work vit new\\Networks Project\\Multiclient\\Main Project\\matrix2.csv"; 
            String ans="";

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                BufferedReader br2 = new BufferedReader(new FileReader(file2));
                String line;
                String hold[]=s.split(" ");
                int st=Integer.parseInt(hold[1]);
                int fin=Integer.parseInt(hold[2]);
                int cc=0;
                System.out.println("Matrix 1");
                int sx=fin-st+1;
                int sy=matrix_addition.yf;
                int mat1[][]=new int[sy][sx];
                int crow=0;
                while ((line = br.readLine()) != null&&crow<sy) {
                    
                    String[] cells = line.split(",");
                    cc=0;
                    int ctr=0;
                    for (String cell : cells) 
                    {
                        if(cc>=st&&cc<=fin)
                        {
                            mat1[crow][ctr]=Integer.parseInt(cell);
                            //System.out.print(cell + "  ");
                            ctr++;
                        }
                        cc++;
                    }
                    crow++;
                    //System.out.println(); 
                }
                cc=0;
                crow=0;
                System.out.println("Matrix 2");

                while ((line = br2.readLine()) != null &&crow<sy) {
                    
                    // Split by comma (basic, not for quoted values)
                    String[] cells = line.split(",");
                    cc=0;
                    int ctr=0;
                    for (String cell : cells) 
                    {
                        if(cc>=st&&cc<=fin)
                        {
                            mat1[crow][ctr]=Integer.parseInt(cell)+mat1[crow][ctr];
                            //System.out.print(cell + "  ");
                            ctr++;
                        }
                        cc++;
                    }
                    crow++;
                    //System.out.println(); // new line after each row
                }

                int i,j;
                
                ans="";
                for(i=0;i<sy;i++)
                {
                    for(j=0;j<sx;j++)
                    {
                        ans+=i+" "+(st+j)+" "+mat1[i][j]+"\n";
                        //System.out.print(mat1[i][j]+" ");
                    }
                    //System.out.println();
                }
                mat1=null;
                
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ans;
        
        }
    }

    static public class matrix_multiplication
    {

        public static int xf=10;
        public static int yf=10; 

        public static String multiply(String s)
        {
            String file = "C:\\Users\\adity\\OneDrive\\Desktop\\All Folders\\college work vit new\\Networks Project\\Multiclient\\Main Project\\matrix_mult_1.csv"; 
            String file2 = "C:\\Users\\adity\\OneDrive\\Desktop\\All Folders\\college work vit new\\Networks Project\\Multiclient\\Main Project\\matrix_mult_2.csv"; 
            String ans="";

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                BufferedReader br2 = new BufferedReader(new FileReader(file2));
                String line;
                String hold[]=s.split(" ");
                int st=Integer.parseInt(hold[1]);
                int fin=Integer.parseInt(hold[2]);
                int cc=0;
                System.out.println("Matrix 1");
                
                int dy=matrix_multiplication.yf;
                int dx=matrix_multiplication.xf;
                int mat1[][]=new int[dy][dx];
                int mat2[][]=new int[dy][dx];
                int crow=0;
                while ((line = br.readLine()) != null) {
                    
                    String[] cells = line.split(",");
                    cc=0;
                    for (String cell : cells) 
                    {
                        mat1[crow][cc]=Integer.parseInt(cell);
                        cc++;
                    }
                    crow++;
                    //System.out.println(); // new line after each row
                }
                crow=0;
                
                while ((line = br2.readLine()) != null) {
                    
                    String[] cells = line.split(",");
                    cc=0;
                    for (String cell : cells) 
                    {
                        mat2[crow][cc]=Integer.parseInt(cell);
                        cc++;
                    }
                    crow++;
                    
                }
                int i,j,k;
                int sx=fin-st+1;
                int sy=matrix_multiplication.yf;
                ans="";
                for(i=0;i<sy;i++)
                {
                    for(j=st;j<=fin;j++)
                    {
                        int sum=0;
                        for(k=0;k<dx;k++)
                        {
                            sum+=mat1[i][k]*mat2[k][j];
                        }
                        ans+=i+" "+(j)+" "+sum+"\n";
                        //System.out.print(mat1[i][j]+" ");
                    }
                    //System.out.println();
                }
                mat1=null;
                
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ans;
        
        }
    }

    public JPanel startscreen(CardLayout cardLayout,JPanel container)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton connectButton = new JButton("Connect");
        JButton waitButton = new JButton("Home");

        Dimension buttonSize = new Dimension(150, 40);
        connectButton.setMaximumSize(buttonSize);
        waitButton.setMaximumSize(buttonSize);

        connectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        waitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(10));
        panel.add(waitButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(connectButton);
        panel.add(Box.createVerticalStrut(10));

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                cardLayout.show(container, "Second");
            }
        });
        waitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                cardLayout.show(container, "Third");
            }
        });

        return panel;
    }

    public JPanel connect_screen(CardLayout cardLayout,JPanel container) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("<-");
        topPanel.add(backButton);
        panel.add(topPanel);

        JPanel inputPanel = new JPanel(new FlowLayout());
        JTextField portField = new JTextField("Port number", 12);
        JButton submitButton = new JButton("Submit");
        inputPanel.add(portField);
        inputPanel.add(submitButton);
        panel.add(inputPanel);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel successLabel = new JLabel("");
        statusPanel.add(successLabel);
        panel.add(statusPanel);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(container, "Third");
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String port = portField.getText();
                    boolean val=connect(port);
                    if(val) successLabel.setText("Connection Successfully established");
                    else successLabel.setText("Connection Failed");
                } catch (Exception ex) {
                    successLabel.setText("Connection Failed");
                }
            }
        });

        return panel;

    }

    public JPanel device_info_screen(CardLayout cardLayout, JPanel container) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("<-");
        topPanel.add(backButton);
        panel.add(topPanel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 10, 2));

        JLabel nameLabel = new JLabel("Current Device Name :");
        JTextField nameField = new JTextField(15);
        nameField.setText(myname);
        nameField.setEditable(false);
        JLabel ipLabel = new JLabel("Current Device IP Address :");
        JTextField ipField = new JTextField(15);
        ipField.setText(myip);
        ipField.setEditable(false);
        JLabel portLabel = new JLabel("Current Device Port Number :");
        JTextField portField = new JTextField(10);
        portField.setText(Integer.valueOf(myport).toString());
        portField.setEditable(false);

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(ipLabel);
        inputPanel.add(ipField);
        inputPanel.add(portLabel);
        inputPanel.add(portField);

        panel.add(inputPanel, BorderLayout.CENTER);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(container, "Third");
            }
        });

        return panel;
    }

    
    public JPanel computing_task_screen(CardLayout cardLayout, JPanel container) {
        JPanel panel = new JPanel(new BorderLayout());
    
        // Top panel with back button and computing task menu
        JPanel topPanel = new JPanel(new BorderLayout());
    
        JButton backButton = new JButton("<-");
        JButton refreshButton = new JButton("Refresh");
        JButton Proceed = new JButton("Proceed");
        topPanel.add(backButton, BorderLayout.WEST);
    
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        String options[]={"Addition","Multiplication","Load_Distribute"};
        JComboBox<String> taskMenu = new JComboBox<>(options);
        menuPanel.add(taskMenu);
        menuPanel.add(refreshButton);
        menuPanel.add(Proceed);
        topPanel.add(menuPanel, BorderLayout.CENTER);
        
        panel.add(topPanel, BorderLayout.NORTH);
    
        // Main panel split into Device Task Allotment and Device Log Terminal
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
    
        // Left panel: Device Task Allotment
        JPanel taskAllotmentPanel = new JPanel();
        taskAllotmentPanel.setLayout(new BorderLayout());
        taskAllotmentPanel.setBorder(BorderFactory.createTitledBorder("Device Task Allotment"));
    
        // Create the task list panel that we'll refresh
        JPanel taskListPanel = new JPanel();
        taskListPanel.setLayout(new GridLayout(0, 2, 5, 10)); // 0 rows means dynamic, 2 columns (device, checkbox)
    
        // Right panel: Device Log Terminal
        JPanel logTerminalPanel = new JPanel(new BorderLayout());
        logTerminalPanel.setBorder(BorderFactory.createTitledBorder("Device Log Terminal"));
    
        JTextArea logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane logScroll = new JScrollPane(logArea);
        logTerminalPanel.add(logScroll, BorderLayout.CENTER);
    
        // Add both subpanels to the main panel
        mainPanel.add(taskAllotmentPanel);
        mainPanel.add(logTerminalPanel);
    
        panel.add(mainPanel, BorderLayout.CENTER);
        Vector<JButton> device_mapper=new Vector<>(connected_devices.size());
        Vector<JCheckBox> taskchecker=new Vector<>(connected_devices.size());
    
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              
                taskListPanel.removeAll();
                

                device_mapper.clear();
                taskchecker.clear();
                
                // Set new grid layout
                taskListPanel.setLayout(new GridLayout(connected_devices.size(), 2, 5, 10));
                
               
                for (int i = 0; i < connected_devices.size(); i++) 
                {
                    
                    device_Database device = connected_devices.get(i);
                    
                    
                    JButton deviceButton = new JButton(device.name);
                    JCheckBox taskCheckbox = new JCheckBox("Allot");
                    
                    device_mapper.add(deviceButton);
                    taskchecker.add(taskCheckbox);
                    
                    // Add to panel
                    taskListPanel.add(deviceButton);
                    taskListPanel.add(taskCheckbox);
                    
                    deviceButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Now we can access the specific device object
                            System.out.println(device.name + " was pressed");
                            System.out.println("Device details: " + device.toString());
                            
                        }
                    });
                }
                
                // Update UI
                taskListPanel.revalidate();
                taskListPanel.repaint();
            }
        });

        Proceed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(computing_flag>0)
                {
                    System.out.println("Already computing");
                    JOptionPane.showMessageDialog(null, "Computing ongoing. Cannot be interrupted", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    String path="device_buffer.txt";
                    try
                    {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(path, false));
                        writer.write("");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    int i;
                    int count=0;
                    for(i=0;i<taskchecker.size();i++)
                    {
                        if(taskchecker.get(i).isSelected())
                        {
                            count++;
                        }
                    }

                    String option=taskMenu.getSelectedItem().toString();
                    int fin=0;
                    int par=0;
                    int flagger=0;
                    switch (option) {
                        case "Addition":
                            par=matrix_addition.xf/count;
                            fin=matrix_addition.xf-1;
                            break;
                        case "Multiplication":
                            par=matrix_multiplication.xf/count;
                            fin=matrix_multiplication.xf-1;
                            break;
                        case "Load_Distribute":
                            {
                                load=100;
                                computing_terminal+="Initial load : "+load+"\n";
                                computing_terminal+="Load allotted to device : "+takeaway+"\n";
                                amount_computed+=takeaway;
                                computing_terminal+="Total load processed by device : "+amount_computed+"\n";
                                load-=takeaway;
                                par=load/count;
                                flagger=1;
                                break;
                            }
                        default:
                            par=matrix_addition.xf/count;
                            fin=matrix_addition.xf-1;
                            break;
                    }
                    int xind=0;
                    int xfin=xind+par-1;
                    int count2=1;

                    for(i=0;i<taskchecker.size();i++)
                    {
                        if(flagger==0)
                        {
                            if(taskchecker.get(i).isSelected())
                            {
                                if(count2==count)
                                {
                                    xind=xind;
                                    xfin=fin;
                                }
                                else
                                {
                                    xind=xind;
                                    xfin=xind+par-1;
                                }
                                connected_devices.get(i).buffer="$$enter_computing_mode$$ "+xind+" "+xfin+" "+taskMenu.getSelectedItem();
                                count2++;
                                xind+=par;
                            }
                        }
                        else
                        {
                            if(taskchecker.get(i).isSelected())
                            {
                                if(count2==count)
                                {
                                    xfin=load;
                                }
                                else
                                {
                                    xfin=par;
                                    load-=par;
                                }
                                connected_devices.get(i).buffer="$$load_dist$$ "+xfin+" "+taskMenu.getSelectedItem();
                                count2++;
                            }
                        }
                    }
                }

            }
        });

        
        Timer timer = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(!computing_terminal.equals(""))
                {
                    logArea.setText(computing_terminal);
                }
            }
        });
        timer.start();

    
        // Back button action
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(container, "Third");
            }
        });

        
    
        // Initial setup
        taskAllotmentPanel.add(taskListPanel, BorderLayout.NORTH);
        return panel;
    }
    

    public JPanel main_screen(CardLayout cardLayout, JPanel container) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
    
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton removeButton = new JButton("Remove");
        JButton connectButton = new JButton("Connect");
        JButton infoButton = new JButton("Info");
        JButton ComputeButton = new JButton("Compute");
        topPanel.add(removeButton);
        topPanel.add(connectButton);
        topPanel.add(infoButton);
        topPanel.add(ComputeButton);
        
        JPanel middlePanel = new JPanel(new GridLayout(1, 2, 10, 10));
    
        JPanel leftPanel = new JPanel(new BorderLayout());
        JPanel devicePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox<device_Database> deviceDropdown = new JComboBox<>(connected_devices);
        deviceDropdown.setPreferredSize(new Dimension(200, 30));

        if (deviceDropdown.getItemCount() > 0) {
            deviceDropdown.setSelectedIndex(0);
        }

        devicePanel.add(new JLabel("Device name"));
        devicePanel.add(deviceDropdown);
        leftPanel.add(devicePanel, BorderLayout.NORTH);

        device_Database selectedDevice = (device_Database) deviceDropdown.getSelectedItem();
        
        JTextArea chatArea = new JTextArea(8, 20);
        chatArea.setEditable(false);
        chatArea.setMargin(new Insets(5, 5, 5, 5));
        leftPanel.add(new JScrollPane(chatArea), BorderLayout.CENTER);


        Timer timer = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                device_Database selectedDevice = (device_Database) deviceDropdown.getSelectedItem();
                if (selectedDevice != null) {
                    chatArea.setText(selectedDevice.message_log);
                }
            }
        });
        timer.start(); 

        


    
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField messageField = new JTextField(35);
        JButton sendButton = new JButton("Send");
        inputPanel.add(messageField);
        inputPanel.add(sendButton);
        leftPanel.add(inputPanel, BorderLayout.SOUTH);
    
        JPanel rightPanel = new JPanel(new BorderLayout());
        
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JTextField filePathField = new JTextField(20);
        filePathField.setEditable(false);
        JButton browseButton = new JButton("Browse");
        JButton uploadButton = new JButton("Upload");

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    filePathField.setText(selectedFile.getAbsolutePath());
                }
            }
        });


        
        // Add components to the file panel
        filePanel.add(new JLabel("Select File: "));
        filePanel.add(filePathField);
        filePanel.add(browseButton);
        filePanel.add(uploadButton);
        
        // Add file panel to infoPanel
        infoPanel.add(filePanel);
        rightPanel.add(infoPanel, BorderLayout.NORTH);
        
        
        JTextArea messageArea = new JTextArea(8, 20);
        messageArea.setEditable(false);
        messageArea.setMargin(new Insets(5, 5, 5, 5));
        Timer timer2 = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                device_Database selectedDevice = (device_Database) deviceDropdown.getSelectedItem();
                if (selectedDevice != null) {
                    int scrollPosition = messageArea.getCaretPosition();
                    int textLength = messageArea.getDocument().getLength();
                    
                    messageArea.setText(selectedDevice.terminal_log);
                    
                    if (scrollPosition == textLength) { 
                        messageArea.setCaretPosition(messageArea.getDocument().getLength());
                    }
                }
            }
        });
        timer2.start(); 
        rightPanel.add(new JScrollPane(messageArea), BorderLayout.CENTER);
        
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filePath = filePathField.getText();
                if (!filePath.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Uploading: " + filePath, "Upload", JOptionPane.INFORMATION_MESSAGE);
                    connected_devices.get(((device_Database) deviceDropdown.getSelectedItem()).pos).terminal_log="";
                    connected_devices.get(((device_Database) deviceDropdown.getSelectedItem()).pos).buffer="$$data_file_transfer$$"+(filePathField.getText());
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a valid file!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        middlePanel.add(leftPanel);
        middlePanel.add(rightPanel);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(middlePanel, BorderLayout.CENTER);

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                cardLayout.show(container, "Second");
            }
        });

        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                cardLayout.show(container, "Fourth");
            }
        });

        
        ComputeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                    cardLayout.show(container, "Fifth");
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {

                if(!messageField.getText().equals("")&&((device_Database) deviceDropdown.getSelectedItem())!=null)
                {
                    System.out.println("inner");
                    String data=messageField.getText();
                    connected_devices.get(((device_Database) deviceDropdown.getSelectedItem()).pos).buffer=data;
                    messageField.setText("");
                }
            }
        });
        
        return panel;
    }

    

    public void setup(device screens)
    {
        JFrame frame = new JFrame("Network Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new CardLayout());

        CardLayout cardlayout=new CardLayout();
        JPanel container=new JPanel(cardlayout);

        JPanel panel1=screens.startscreen(cardlayout,container);
        JPanel panel2=screens.connect_screen(cardlayout,container);
        JPanel panel3=screens.main_screen(cardlayout,container);
        JPanel panel4=screens.device_info_screen(cardlayout,container);
        JPanel panel5=screens.computing_task_screen(cardlayout,container);
        container.add(panel1,"First");
        container.add(panel2,"Second");
        container.add(panel3,"Third");
        container.add(panel4,"Fourth");
        container.add(panel5,"Fifth");

        frame.add(container);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    ServerSocket serverSocket=null;
    Socket acceptsocket=null;
    Socket sendsocket=null;
    BufferedReader br=null;

    static String myname,myip;
    static int myport;

    public static Vector<device_Database> connected_devices=new Vector<>();

    public static device_Database assign_info(Socket socket,int pn)
        {
            device_Database obj;
            obj=new device_Database();
            try
            {
                DataInputStream in=new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                DataOutputStream out=new DataOutputStream(socket.getOutputStream());
                obj.pn=pn;
                obj.in=in;
                obj.out=out;
                obj.socket=socket;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return obj;
        }

    static void initialise_threads(Socket socket,int pn)
    {
        try
        {
            device_Database holder=assign_info(socket,pn);
                    holder.pos=connected_devices.size();
                     connected_devices.add(holder);
                     holder.out.writeUTF(myname);
                     holder.out.writeUTF(myip);

                    Read obj=new Read(holder);
                     Write obj2=new Write(holder);

                     Thread t1=new Thread(obj);
                     Thread t2=new Thread(obj2);

                     t1.start();
                     t2.start();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    static boolean connect(String s)
    {
        try
        {
            int port_connect=Integer.parseInt(s);
            Socket socket=new Socket("127.0.0.1",port_connect);
            System.out.println("Connection established");
            initialise_threads(socket, port_connect);
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }

    }


    static class OpenConnection implements Runnable
    {
        int port;
        OpenConnection(int pn)
        {
            port=pn;
        }

        public void run()
        {
            try
            {
                ServerSocket serverSocket =new ServerSocket(port);

                while(true)
                {
                    Socket socket=serverSocket.accept();
                    System.out.println("Connection established");
                    initialise_threads(socket, port);
                }

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    static class Read implements Runnable
    {
        device_Database currentDevice;
        int pos;

        Read(device_Database obj)
        {
            currentDevice=obj;
            pos=currentDevice.pos;
        }

        public void read_data(String s)
        {
            if(s.equals("$$Codeword_buffer_dont_display$$")) return;
            connected_devices.get(pos).message_log+=currentDevice.name+" : "+s+"\n";
            System.out.println(connected_devices.get(pos).message_log);
        }

        public String read_message(String s)
        {
            String parts[]=s.split(" ");
            int seq=Integer.parseInt(parts[0]);
            seq=seq+1;
            return seq+" "+parts[1];
        }

        public void activate_goback_n(String s)
        {
            device_Database holderobj=connected_devices.get(pos);
            DataInputStream in=holderobj.in;
            connected_devices.get(pos).terminal_log+="Mode1 :  Entering file transfer mode with "+holderobj.name+"\n";
            try
            {
                String st="";
                int bef=-1;
                int ct=0;
                DataOutputStream dataout=connected_devices.get(currentDevice.pos).out;
                dataout.writeUTF("$$Codeword_buffer_dont_display$$");
                while(true)
                {
                    st=in.readUTF();
                    if(st.equals("$$end_file_transfer$$")) break;
                    connected_devices.get(pos).terminal_log+=holderobj.name+" : "+st+"\n";

                    String arr[]=st.split(" ");
                    int seq=Integer.parseInt(arr[0]);
                    if(seq==bef+1)
                    {
                        st="Acknowledgement";
                        String pt=Integer.valueOf(seq+1).toString();
                        bef++;
                        System.out.println("Acknowledgement sent : "+st+" "+pt);
                        dataout.writeUTF(st+" "+pt);
                    }
                    else
                    {
                        System.out.println("Packet discarded");
                        st="Acknowledgement";
                        String pt=Integer.valueOf(bef+1).toString();
                        dataout.writeUTF(st+" "+pt);
                    }
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        public void start_computing(String s)
        {
            try
            {
                System.out.println("Member of computing");
                computing_terminal+="Added member\n";
                DataOutputStream out=connected_devices.get(currentDevice.pos).out;

                out.writeUTF("$$Codeword_buffer_dont_display$$");

                
                System.out.println("Comp not done");

                String parts[]=s.split(" ");
                String opt=parts[parts.length-1];
                String hold="";
                switch(opt)
                {
                    case "Addition":
                    {
                        hold=device.matrix_addition.add(s);
                        break;
                    }
                    case "Multiplication":
                    {
                        hold=device.matrix_multiplication.multiply(s);
                        break;
                    }
                    default:
                    {
                        hold=device.matrix_addition.add(s);
                        break;
                    }
                }

                System.out.println(hold);

                String send[]=hold.split("\n");

                String path="device_buffer.txt";
                mutex.acquire();
                BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
                try
                {
                    for (String line : send) {
                        System.out.print("writing - "+line);
                        writer.write(line);
                        writer.newLine();
                    }
                    System.out.println("File cleared and written.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally
                {
                    writer.close();
                    mutex.release();
                }
                //out.writeUTF(hold);
                computing_terminal+="Computing tasked by "+currentDevice.name+" complete\n";
                out.writeUTF("Comp done");
                System.out.println("Comp done");

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        public void load_distributor(String s)
        {
            String parts[]=s.split(" ");
            int l=Integer.parseInt(parts[1]);
            if(l<=0) return;
            load=l;
            computing_terminal+="Incoming load  from "+currentDevice.name+" : "+load+"\n";
            if(load<=takeaway)
            {
                computing_terminal+="Task load allotted to current device : "+load+"\n";
                amount_computed+=load;
                computing_terminal+="Load computed from beginnning : "+amount_computed+"\n";
                computing_terminal+=
                load=0;
                return;
            }
            else
            {
                computing_terminal+="Task load allotted to current device : "+takeaway+"\n";
                amount_computed+=takeaway;
                computing_terminal+="Load computed from beginnning : "+amount_computed+"\n";
            }

            int store=load-takeaway;
            if(store<=0) return;
            load=takeaway;
            try
            {
                int i,j;
                int par=store/connected_devices.size();
                for(i=0;i<connected_devices.size();i++)
                {
                    DataOutputStream out=connected_devices.get(i).out;
                    int assign=0;
                    if(i==connected_devices.size()-1)
                    {
                        assign=store;
                    }
                    else
                    {
                        assign=par;
                    }
                    store-=assign;
                    computing_terminal+="Task of load : "+assign+" assigned to "+connected_devices.get(i).name+"\n";
                    out.writeUTF("$$load_dist$$ "+assign);
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }


        }

        public void read_function_mapper(String s)
        {
            try
            {
                if(s.indexOf("$$data_file_transfer$$")!=-1)
                {
                    activate_goback_n(s);
                }
                else if(s.indexOf("$$enter_computing_mode$$")!=-1)
                {
                    start_computing(s);
                }
                else if(s.indexOf("$$load_dist$$")!=-1)
                {
                    load_distributor(s);
                }
                else
                {
                    System.out.println("Entering read : "+s);
                    read_data(s);
                }

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        public void run()
        {
            try
            {
                
                String s;
                
                DataInputStream in=currentDevice.in;
                currentDevice.name=in.readUTF();
                currentDevice.ip=in.readUTF();

                while(true)
                {
                    if(connected_devices.get(currentDevice.pos).file_transfer_flag==0)
                    {
                        
                        s=in.readUTF();
                        System.out.println("function called "+s);
                        read_function_mapper(s);
                        System.out.println("After function "+s);
                        
                    }
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    static class Write implements Runnable
    {
        device_Database currentdevice;
        int pos;
        DataOutputStream out=null;
        Write(device_Database obj)
        {
            currentdevice=obj;
            pos=currentdevice.pos;
            out=currentdevice.out;
        }




        public void transmit(int li,int ui, Vector<String> message,Vector<String> seq,int sf)
            {

                int i;
                try
                {
                    DataOutputStream dataout=connected_devices.get(currentdevice.pos).out;
                    for(i=li;i<=ui;i++)
                        {
                            String s=message.get(i);
                            String send=seq.get(i)+" "+s;

                            System.out.println("Message sent : "+send);
                            dataout.writeUTF(send); 
                        }

                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }

        public void activate_goback_n(String s)
        {
            try
            {
                String filep=s.substring(22,s.length());
                device_Database holderobj=connected_devices.get(pos);
                out.writeUTF(s);
                connected_devices.get(pos).terminal_log+="Mode1 :  Entering file transfer mode with "+filep+" "+holderobj.name+"\n";
                FileReader fr=new FileReader(filep);
                BufferedReader br=new BufferedReader(fr);

                String all_data="";

                Vector<String> messages=new Vector<>();
                Vector<String> sequence=new Vector<>();

                String word;
                int ctr=0;
                while((word=br.readLine())!=null)
                {
                    String words[]=word.split("\\s+");
                    for(String travel:words)
                    {
                        //out.writeUTF(travel);
                        messages.add(travel);
                        sequence.add(Integer.valueOf(ctr).toString());
                        ctr++;
                    }
                }

                int N=4;
                int l=messages.size();
                int li=0;
                int ui=(li+N-1>=l)?l-1:li+N-1;
                ctr=0;
                int sf=0;
                int record=-1;

                transmit(li, ui, messages, sequence, sf);
                ctr=ui+1;
                String buff;
                Socket socket=connected_devices.get(currentdevice.pos).socket;
                DataInputStream datain=connected_devices.get(currentdevice.pos).in;

                while(true)
                {
                    socket.setSoTimeout(5000);
                    try
                    {
                        buff=datain.readUTF();
                        System.out.println("Server : "+buff);
                        connected_devices.get(currentdevice.pos).terminal_log+=currentdevice.name+" : "+buff+"\n";
                        String sh[]=buff.split(" ");
                        int holder=Integer.parseInt(sh[1])-1;
                        if(holder>=record+1)
                        {
                            record=holder;
                            sf=record+1;
                            li=record+1;
                            connected_devices.get(currentdevice.pos).terminal_log+="Acknowledgement accepted\n";
                            ui=((li+N-1)>(l-1))?l-1:li+N-1;
                        }
                        else
                        {
                            System.out.println("Acknowledgement discarded");
                            connected_devices.get(currentdevice.pos).terminal_log+="Acknowledgement discarded\n";
                            continue;
                        }
                    }
                    catch(Exception e)
                    {
                        System.out.println("Acknowledgement timeout");
                        System.out.println("Unsuccessful");
                        connected_devices.get(currentdevice.pos).terminal_log+="Acknowledgement timeout\n";
                        transmit(li, ui, messages, sequence,sf);
                        ctr=ui+1;
                        continue;
                    }
                    finally
                    {
                        socket.setSoTimeout(0);
                    }

                    if(record==l-1)
                    {
                        out.writeUTF("$$end_file_transfer$$");
                        break;
                    }
                    if(ctr==l) continue;
                    transmit(ctr, ctr, messages, sequence, sf);
                    ctr++;
                    if(buff.equals("$$end_file_transfer$$"))
                    {
                        break;
                    }
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        public void computing_coordinator(String s)
        {
            try
            {
                out.writeUTF(s);
                System.out.println("Computing mode begin");
                computing_terminal+="Coordinating server initiated with "+connected_devices.get(pos).name+"\n";
                computing_terminal+="Computing started by "+currentdevice.name+"\n";
                computing_flag++;

                DataInputStream in=connected_devices.get(pos).in;
                DataOutputStream out=connected_devices.get(pos).out;
                String st;
                System.out.println("Computing message : "+s);
                st=in.readUTF();
                computing_flag--;
                computing_terminal+="Computing completed by "+currentdevice.name+"\n";
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        public void write_data(String s)
        {
            try
            {
                connected_devices.get(pos).message_log+="Me : "+s+"\n";
                out.writeUTF(s);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        public void load_distribute_write(String s)
        {
            String parts[]=s.split(" ");
            int l=Integer.parseInt(parts[1]);
            computing_terminal+="Task of load : "+l+" assigned to "+currentdevice.name+"\n";
            try
            {
                out.writeUTF("$$load_dist$$ "+l);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        public void write_function_mapper(String s)
        {
            try
            {

                if(s.indexOf("$$data_file_transfer$$")!=-1)
                {
                    System.out.println("Entering file transfer : "+s);
                    connected_devices.get(currentdevice.pos).file_transfer_flag=1;
                    activate_goback_n(s);
                    connected_devices.get(currentdevice.pos).file_transfer_flag=0;
                    System.out.println("Leaving file transfer : "+s);
                }
                else if(s.indexOf("$$enter_computing_mode$$")!=-1)
                {
                    computing_coordinator(s);
                }
                else if(s.indexOf("load_dist")!=-1)
                {
                    load_distribute_write(s);
                }
                else
                {
                    System.out.println("Entering write : "+s);
                    write_data(s);
                }

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }



        public void run()
        {
            try
            {
                DataOutputStream out=currentdevice.out;
                BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
                String s;
                while(true)
                {
                    if(!connected_devices.get(pos).buffer.equals(""))
                    {
                        
                        s=connected_devices.get(pos).buffer;
                        System.out.println("Write thread function begin : "+s);
                        write_function_mapper(s);
                        System.out.println("Write thread function end : "+s);
                        connected_devices.get(pos).buffer="";
                        
                    }
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    static String generate_name(int length)
    {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom RANDOM = new SecureRandom();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public static void main(String[] args) 
    {
            computing_terminal="";
            computing_flag=0;
          int pn=ThreadLocalRandom.current().nextInt(0, 65535);
          int p1=ThreadLocalRandom.current().nextInt(0, 255);
          int p2=ThreadLocalRandom.current().nextInt(0, 255);
          int p3=ThreadLocalRandom.current().nextInt(0, 255);
          int p4=ThreadLocalRandom.current().nextInt(0, 255);
          myport=pn;
          myname=generate_name(32);
          myip=p1+"."+p2+"."+p3+"."+p4;

          OpenConnection openconnect=new OpenConnection(pn);
          Thread t=new Thread(openconnect);
          t.start();

        device screens=new device();
        screens.setup(screens);
        
    }
}
