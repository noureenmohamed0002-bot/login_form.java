import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

class User {

    private String username;
    private String password;
    private String role;

    User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}

class WelcomePage extends Frame {

    WelcomePage(User user) {
        super("Welcome");

        setLayout(new GridLayout(2, 1, 10, 10));

        Label hello = new Label(
            "Hello, " + user.getUsername(),
            Label.CENTER
        );

        Label userRole = new Label(
            "Your role is: " + user.getRole(),
            Label.CENTER
        );

        hello.setFont(new Font("SansSerif", Font.BOLD, 20));

        add(hello);
        add(userRole);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(350, 180);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

public class LoginFormTask extends Frame {

    private ArrayList<User> users = new ArrayList<>();

    private int wrongAttempts = 0;
    private boolean loginLocked = false;

    private Button login;

    public LoginFormTask() {
        super("Login");

        loadUsers();

        setLayout(new GridBagLayout());

        Panel content = new Panel(new BorderLayout(10, 15));

        Label title = new Label("Welcome back", Label.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        content.add(title, BorderLayout.NORTH);

        Panel form = new Panel(new GridLayout(3, 2, 8, 8));

        form.add(new Label("Username:", Label.RIGHT));
        TextField username = new TextField(18);
        form.add(username);

        form.add(new Label("Password:", Label.RIGHT));
        TextField password = new TextField(18);
        password.setEchoChar('*');
        form.add(password);

        form.add(new Label("Role:", Label.RIGHT));
        Choice role = new Choice();
        role.add("Student");
        role.add("TA");
        role.add("Professor");
        form.add(role);

        content.add(form, BorderLayout.CENTER);

        Panel buttons = new Panel(
            new FlowLayout(FlowLayout.CENTER, 15, 0)
        );

        Button cancel = new Button("Cancel");
        login = new Button("Login");

        buttons.add(cancel);
        buttons.add(login);

        content.add(buttons, BorderLayout.SOUTH);

        add(content);

        // Login logic
        login.addActionListener(e -> {

            // Stop if login is locked
            if (loginLocked) {
                return;
            }

            String enteredUsername = username.getText().trim();
            String enteredPassword = password.getText();
            String enteredRole = role.getSelectedItem();

            // Check empty fields
            if (enteredUsername.isEmpty()
                    || enteredPassword.isEmpty()) {

                JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all fields."
                );

                return;
            }

            // Find user
            User user = findUser(enteredUsername);

            // Check username and role
            if (user == null
                    || !user.getRole().equals(enteredRole)) {

                JOptionPane.showMessageDialog(
                    this,
                    "Invalid username or role."
                );

                return;
            }

            // Check password
            if (!user.getPassword().equals(enteredPassword)) {

                wrongAttempts++;

                if (wrongAttempts >= 3) {

                    loginLocked = true;

JOptionPane.showMessageDialog(
                        this,
                        "You entered the wrong password 3 times.\n"
                        + "Login is now locked!"
                    );

                    login.setEnabled(false);
                    username.setEnabled(false);
                    password.setEnabled(false);
                    role.setEnabled(false);

                } else {

                    JOptionPane.showMessageDialog(
                        this,
                        "Incorrect password. Attempts left: "
                        + (3 - wrongAttempts)
                    );
                }

                return;
            }

            // Successful login
            JOptionPane.showMessageDialog(
                this,
                "Login successful!"
            );

            new WelcomePage(user);
            dispose();
        });

        // Cancel button
        cancel.addActionListener(e -> dispose());

        // Close window
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(420, 340);
        setLocationRelativeTo(null);
    }

    // Hard-coded users
    private void loadUsers() {
        users.add(new User("student1", "2222", "Student"));
        users.add(new User("ta1", "0000", "TA"));
        users.add(new User("prof1", "5555", "Professor"));
    }

    // Search for a user by username
    private User findUser(String name) {

        for (User user : users) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }

        return null;
    }

    public static void main(String[] args) {
        new LoginFormTask().setVisible(true);
    }
}