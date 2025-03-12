import java.util.Arrays;
import java.util.Comparator;

class Student implements Comparable<Student> {
    private String name;
    private int age;
    private int grade;

    public Student() {

    }

    public Student(String name, int age, int grade) {
        this.name = name;
        this.age = age;
        this.grade = grade;
    }

    public Student(Student s) {
        this.name = s.name;
        this.age = s.age;
        this.grade = s.grade;
    }

    public String GetName() {
        return name;
    }

    public int GetAge() {
        return age;
    }

    public int GetGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return "Student{name='" + name + "', age=" + age + ", grade=" + grade + "}";
    }

    @Override
    public Student clone() {
        return new Student(this);
    }

    @Override
    public int compareTo(Student o) {
        return Integer.compare(this.grade, o.grade);
    }
}

class StudentClass {
    private Student[] v;
    private int n;
    private final int N = 10;

    public StudentClass() {
        this.n = N;
        this.v = new Student[n];
    }

    public StudentClass(int n) {
        this.n = n;
        this.v = new Student[n];
    }

    public void Add(Student s) {
        for (int i = 0; i < v.length; i++) {
            if (v[i] == null) {
                v[i] = s;
                break;
            }
        }
    }

    public void Print() {
        for (Student s : v) {
            if (s != null) {
                System.out.println(s);
            }
        }
    }

    public void Sort() {
        Arrays.sort(v, Comparator.nullsLast(Comparator.naturalOrder()));
    }

    public int Getn() {
        return n;
    }
}

public class Main {
    public static void main(String[] args) {
        StudentClass sc = new StudentClass(3);
        sc.Add(new Student("Alice", 20, 85));
        sc.Add(new Student("Bob", 22, 90));
        sc.Add(new Student("Charlie", 21, 80));

        System.out.println("Inainte de sortare:");
        sc.Print();

        sc.Sort();

        System.out.println("Dupa sortare:");
        sc.Print();
    }
}
