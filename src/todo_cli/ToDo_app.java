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


public class ToDo_app {
    private CSVReader reader;
    private CSVWriter writer;
    private static List<String[]> notes =  new ArrayList<>();
    private final String csv_name = "ToDo_app.csv";
    private final DateFormat formatter_deadline = new SimpleDateFormat("dd/MM/yyyy");
    private final DateTimeFormatter formatter_localdate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ToDo_app()  {
        try {
            this.writer = new CSVWriter(new FileWriter(this.csv_name, true));
            this.reader = new CSVReader(new FileReader(this.csv_name));
            notes = reader.readAll();
            this.writer.close();
            this.reader.close();
        }
        catch(FileNotFoundException e) {
            System.err.println("CSV file not found, creating new one...");
        }
        catch (IOException e) {
            System.err.println("Error opening CSV file: " + e.getMessage());
            System.exit(1);
        }
        catch (CsvException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            System.exit(1);
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
            String[] note = {title, body, creationdate, f_deadline, String.join(",", tags.split(" "))};
            notes.add(note);
            System.out.println("note created");
        }
        catch (ParseException e){
            System.err.println("Error: invalid deadline format: " + e.getMessage());
        }
        catch (PatternSyntaxException e){
            System.err.println("Error parsing tags: " + e.getMessage());
        }
    }

    public void delete_note(int id) throws IndexOutOfBoundsException {
        notes.remove(id);
        System.out.println("note deleted");
    }

    public void set_deadline(int id, String date) throws IndexOutOfBoundsException {
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

    public void change_title(int id, String title) throws IndexOutOfBoundsException {
        String[] note = notes.get(id);
        note[0] = title;
        notes.set(id, note);
        System.out.println("title changed");
    }

    public void change_body(int id, String body) throws IndexOutOfBoundsException {
        String[] note = notes.get(id);
        note[1] = body;
        notes.set(id, note);
        System.out.println("body changed");
    }

    public void change_tags(int id, String tags) throws IndexOutOfBoundsException {
        String[] note = notes.get(id);
        note[4] = String.join(",", tags.split(" "));
        notes.set(id, note);
        System.out.println("tags changed");
    }

    public void add_tags(int id, String tags) throws IndexOutOfBoundsException {
        String[] note = notes.get(id);
        note[4] += ","+String.join(",", tags.split(" "));
        notes.set(id, note);
        System.out.println("tags added");
    }

    public void sort_ByTitle() {
        notes.sort(Comparator.comparing(note -> note[0]));
        System.out.println("sorted by title");
    }

    public void sort_ByCreationDate() {
        notes.sort(Comparator.comparing(note -> LocalDate.parse(note[2], formatter_localdate)));
        System.out.println("sorted by creation date");
    }

    public void search_ByTags(String tags) {
        HashSet<String> tagSet = new HashSet<>(Arrays.asList(tags.split(" ")));
        for(int i=0; i<notes.size(); i++){
            String[] note = notes.get(i);
            HashSet<String> note_tags = new HashSet<>(Arrays.asList(note[4].split(",")));
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
            System.out.println("notes saved");
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

    public boolean is_deadline_setted(int id) throws IndexOutOfBoundsException {
        String[] note = notes.get(id);
        return !note[3].isEmpty();
    }
}
