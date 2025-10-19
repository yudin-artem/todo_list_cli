package todo_cli;

import com.opencsv.CSVWriter;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.text.DateFormat;
import java.time.LocalDate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;
import java.util.regex.PatternSyntaxException;

//ПОНЯТЬ ЧТО ДЕЛАТЬ С catch И КАК ВОЗВРАЩАТЬ ОШИБКИ
//добавить проверку сепаратора тэгов
//написать trows для методов и их обработчики в main
public class ToDo_app {
    private CSVReader reader;
    private CSVWriter writer;
    private static List<String[]> notes =  new ArrayList<>();
    private final String csv_name = "ToDo_app.csv";
    private final DateFormat formatter_deadline = new SimpleDateFormat("dd/MM/yyyy");
    private final DateTimeFormatter formatter_localdate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ToDo_app() {
        try {
            this.writer = new CSVWriter(new FileWriter(this.csv_name, true));
            this.reader = new CSVReader(new FileReader(this.csv_name));
            notes = reader.readAll();
            this.writer.close();
            this.reader.close();
        } catch (IOException e) {
            System.err.println("Error opening CSV file: " + e.getMessage());
        } catch (CsvException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }

    public void create_note(String title, String body, String deadline, String tags) {
        try {
            String f_deadline;
            if (!deadline.isEmpty()) {
                f_deadline = formatter_deadline.format(formatter_deadline.parse(deadline));
            } else {
                f_deadline = "";
            }
            String creationdate = LocalDate.now().format(formatter_localdate);
            String[] note = {title, body, creationdate, f_deadline, String.join(";", tags.split(" "))};
            notes.add(note);
        } catch (ParseException e){
            System.err.println("Error parsing deadline date: " + e.getMessage());
            return;
        }
        catch (PatternSyntaxException e){
            System.err.println("Error parsing tag pattern: " + e.getMessage());
            return;
        }
        System.out.println("note created");
    }

    public void delete_note(int id) {
        if (id >= 0 && notes.size() > id) {
            notes.remove(id);
            System.out.println("note deleted");

        } else{
            System.out.println("note not found");
        }
    }

    public void set_deadline(int id, String date) {
        try{
            String f_deadline = formatter_deadline.format(formatter_deadline.parse(date));
            String[] note = notes.get(id);
            note[3] = f_deadline;
            notes.set(id, note);
        } catch (ParseException e) {
            System.err.println("Error parsing deadline date: " + e.getMessage());
        }

        System.out.println("deadline set");
    }

    public void change_title(int id, String title) {
        String[] note = notes.get(id);
        note[0] = title;
        notes.set(id, note);
        System.out.println("title changed");
    }

    public void change_body(int id, String body) {
        String[] note = notes.get(id);
        note[1] = body;
        notes.set(id, note);
        System.out.println("body changed");
    }

    public void change_tags(int id, String tags) {
        String[] note = notes.get(id);
        note[4] = String.join(";", tags.split(" "));
        notes.set(id, note);
        System.out.println("tags changed");
    }

    public void add_tags(int id, String tags) {
        String[] note = notes.get(id);
        note[4] += ";"+String.join(";", tags.split(" "));
        notes.set(id, note);
        System.out.println("tags added");
    }

    public void sort_ByTitle() {
        notes.sort(Comparator.comparing(note -> note[0]));
        System.out.println("sorted by title");
    }

    public void sort_ByCreationDate() {
        notes.sort(Comparator.comparing(note -> LocalDate.parse(note[2])));
        System.out.println("sorted by creation date");
    }

    public void search_ByTags(String tags) {
        HashSet<String> tagSet = new HashSet<>(Arrays.asList(tags.split(" ")));
        for(int i=0; i<notes.size(); i++){
            String[] note = notes.get(i);
            HashSet<String> note_tags = new HashSet<>(Arrays.asList(note[4].split(";")));
            if (note_tags.containsAll(tagSet)) {
                System.out.println((i + 1) + ". " + note[0] + "\n" + note[1] + "\n" + "Date of creation: " + note[2] +
                        " | Date of deadline" + note[3] + "\n" + "Tags: " + note[4]);
            }
        }
    }

    public void search_ByTitle(String title) {
        boolean found = false;
        for(int i=0; i<notes.size(); i++){
            String[] note = notes.get(i);
            if (note[0].contains(title)) {
                System.out.println((i + 1) + ". " + note[0] + "\n" + note[1] + "\n" + "Date of creation: " + note[2] +
                        " | Date of deadline" + note[3] + "\n" + "Tags: " + note[4]);
                found = true;
            }
        }
        if(!found){
            System.out.println("Title not found");
        }
    }

    public void save_notes() {
        try {
            this.writer = new CSVWriter(new FileWriter(this.csv_name));
            writer.writeAll(notes);
            this.writer.close();
        }
        catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    public void print_notes() {
        if (notes.isEmpty()) {
            System.out.println("Notes not found\n" +
                    "-----------------------------------------------------------------");
            return;
        }
        for (int i = 0; i<notes.size(); i++) {
            String[] note = notes.get(i);
            System.out.println((i + 1) + ". " + note[0] + "\n" + note[1] + "\n" + "Date of creation: " + note[2] +
                    " | Date of deadline: " + note[3] + "\n" + "Tags: " + note[4] + "\n" +
                    "-----------------------------------------------------------------");
        }
    }

    public boolean is_deadline_setted(int id) {
        String[] note = notes.get(id);
        return !note[3].isEmpty();
    }
}
