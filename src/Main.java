
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String message) {
        super(message);
    }
}

class DuplicateStudentException extends Exception {
    public DuplicateStudentException(String message) {
        super(message);
    }
}

class Student {
    private int id;
    private int rollNo;
    private int age;
    private String name;
    private String course;

    public Student(int id, int rollNo, int age, String name, String course) {
        this.id = id;
        this.rollNo = rollNo;
        this.age = age;
        this.name = name;
        this.course = course;
    }

    public int getId() {
        return id;
    }

    public int getRollNo() {
        return rollNo;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public void setRollNo(int rollNo) {
        this.rollNo = rollNo;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return id + " " + rollNo + " " + age + " " + name + " " + course;
    }
}

class StudentRepository {
    private final HashMap<Integer, Student> students = new HashMap<>();


    public Collection<Student> getAllStudents() {
        return students.values();
    }

    public void loadStudents(Student student) {
      students.put(student.getId(), student);
    }

    public void addStudents(Student student)
            throws DuplicateStudentException {

        if (students.containsKey(student.getId())) {
            throw new DuplicateStudentException("Student already exists");
        }

        students.put(student.getId(), student);
    }

    public Student getStudent(int id) {
        return students.get(id);
    }

    public void deleteStudent(int id) throws StudentNotFoundException {
        if (!students.containsKey(id)) {
            throw new StudentNotFoundException("Student doesn't exist");
        }

        students.remove(id);
        System.out.println("Student deleted successfully.");
    }

    public void viewStudents() {
        if (students.isEmpty()) {
            System.out.println("Student list is empty");
            return;
        }

        for (Student student : students.values()) {
            System.out.println(student);
        }
    }

    public void searchStudent(int id) throws StudentNotFoundException {
        Student student = students.get(id);

        if (student == null) {
            throw new StudentNotFoundException("Student doesn't exist");
        }

        System.out.println(student);
    }

    public void viewCourse() {
        Set<String> courseSet = new HashSet<>();

        for (Student student : students.values()) {
            courseSet.add(student.getCourse());
        }

        if (courseSet.isEmpty()) {
            System.out.println("Course list is empty");
            return;
        }

        for (String course : courseSet) {
            System.out.println(course);
        }
    }

    public void viewStudentCourse(int id) throws StudentNotFoundException {
        Student student = students.get(id);

        if (student == null) {
            throw new StudentNotFoundException("Student doesn't exist");
        }

        System.out.println(student.getName() + "'s course: " + student.getCourse());
    }
}

class StudentManagement {
    private final Scanner sc;
    public StudentRepository repository;
    private static final Path FILE = Path.of("NewStudents.txt");

    public StudentManagement(Scanner sc, StudentRepository repository) {
        this.sc = sc;
        this.repository = repository;
        loadStudents();
    }


    public void loadStudents() {
        if (!Files.exists(FILE)) {
           return;
        }
        try {
            for (String line : Files.readAllLines(FILE)) {
                String[] data = line.split(",");
                int id = Integer.parseInt(data[0]);
                int rollNo = Integer.parseInt(data[1]);
                int age = Integer.parseInt(data[2]);
                String name = data[3];
                String course = data[4];
                Student student = new Student(id, rollNo, age, name, course);
                repository.loadStudents(student);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        }

        public void saveStudents() {
         List<String> lines = new ArrayList<>();
         for (Student s : repository.getAllStudents() ) {
             lines.add(s.getId() + "," + s.getRollNo() + "," + s.getAge() + "," + s.getName() + "," + s.getCourse());
         }
         try {
             Files.write(FILE,lines);
         } catch (IOException e) {
             System.out.println("Error writing file: " + e.getMessage());
         }

        }


    private int readInt() {
        while (true) {
            try {
                int number = sc.nextInt();
                sc.nextLine();
                return number;
            } catch (InputMismatchException e) {
                System.out.print("Please enter a valid number: ");
                sc.nextLine();
            }
        }
    }

    public void addStudent() {
        System.out.println("Enter student ID:");
        int id = readInt();

        System.out.println("Enter roll number:");
        int rollNo = readInt();

        System.out.println("Enter age:");
        int age = readInt();

        System.out.println("Enter name:");
        String name = sc.nextLine();

        System.out.println("Enter course:");
        String course = sc.nextLine();

        Student student = new Student(id, rollNo, age, name, course);

        try {
            repository.addStudents(student);
            saveStudents();
            System.out.println("Student added successfully.");
        } catch (DuplicateStudentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateStudents() {
        System.out.println("Enter student ID:");
        int id = readInt();

        Student student = repository.getStudent(id);

        if (student == null) {
            System.out.println("Student doesn't exist");
            return;
        }

        boolean running = true;

        while (running) {
            System.out.println("\nWhat do you want to update?");
            System.out.println("1. Name");
            System.out.println("2. Roll Number");
            System.out.println("3. Age");
            System.out.println("4. Course");
            System.out.println("5. Exit");

            int userInput = readInt();

            switch (userInput) {
                case 1:
                    System.out.println("Enter student name:");
                    student.setName(sc.nextLine());
                    System.out.println("Name updated.");
                    break;

                case 2:
                    System.out.println("Enter student roll number:");
                    student.setRollNo(readInt());
                    System.out.println("Roll number updated.");
                    break;

                case 3:
                    System.out.println("Enter student age:");
                    student.setAge(readInt());
                    System.out.println("Age updated.");
                    break;

                case 4:
                    System.out.println("Enter student course:");
                    student.setCourse(sc.nextLine());
                    System.out.println("Course updated.");
                    break;

                case 5:
                    running = false;
                    System.out.println("Returning to menu");
                    break;

                default:
                    System.out.println("Invalid input");
                    break;
            }
            saveStudents();
        }
    }

    public void deleteStudent() {
        System.out.println("Enter student ID:");
        int id = readInt();

        try {
            repository.deleteStudent(id);
            saveStudents();
        } catch (StudentNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }


    public void menu() {
        
        boolean running = true;

        while (running) {
            System.out.println("\n===== Student Management System =====");
            System.out.println("1. Add Students");
            System.out.println("2. View Students");
            System.out.println("3. Delete Student");
            System.out.println("4. Search Student");
            System.out.println("5. Update Student");
            System.out.println("6. View Courses");
            System.out.println("7. View Student Course");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");

            int option = readInt();

            switch (option) {
                case 1:
                    addStudent();
                    break;

                case 2:
                    repository.viewStudents();
                    break;

                case 3:
                    deleteStudent();
                    break;

                case 4:
                    System.out.println("Enter student ID:");
                    int searchId = readInt();

                    try {
                        repository.searchStudent(searchId);
                    } catch (StudentNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 5:
                    updateStudents();
                    break;

                case 6:
                    repository.viewCourse();
                    break;

                case 7:
                    System.out.println("Enter student ID:");
                    int courseId = readInt();

                    try {
                        repository.viewStudentCourse(courseId);
                    } catch (StudentNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 8:
                    running = false;
                    System.out.println("Thank you for using the service");
                    break;

                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        StudentRepository repository = new StudentRepository();
        StudentManagement studentManagement =
                new StudentManagement(sc, repository);

        studentManagement.menu();

        sc.close();
    }
}
