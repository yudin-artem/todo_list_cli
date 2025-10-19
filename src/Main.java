import todo_cli.ToDo_app;

import java.util.Scanner;


//добавить сохранение файла в случае ошибки
public class Main {
    public static void main(String[] args) {
        ToDo_app app = new ToDo_app();
        Scanner scanner = new Scanner(System.in);
        app.sort_ByCreationDate();
        app.print_notes();

        boolean exit = false;
        while(!exit){
            try {
                System.out.println("Choose an option:");
                System.out.println("1: Get notes, 2: Add note, 3: Delete note, 4: Change note, 5: Set deadline to note, 6: Sort notes, 7: Search notes, 8: Exit.");
                int choice = Integer.parseInt(scanner.nextLine());
                int id;
                String title, body, deadline, tags;
                switch (choice) {
                    case 1:
                        app.print_notes();
                        break;
                    case 2:
                        System.out.println("Enter note title:");
                        title = scanner.nextLine();
                        System.out.println("Enter note body:");
                        body = scanner.nextLine();
                        System.out.println("Enter note deadline in format dd/mm/yyyy (pass it if you need):");
                        deadline = scanner.nextLine();
                        System.out.println("Enter note tags(pass it if you need):");
                        System.out.println("Separate it with spaces:");
                        tags = scanner.nextLine();
                        app.create_note(title, body, deadline, tags);
                        break;
                    case 3:
                        System.out.println("Enter note number:");
                        id = Integer.parseInt(scanner.nextLine()) - 1;
                        app.delete_note(id);
                        break;
                    case 4:
                        System.out.println("Enter note number:");
                        id = Integer.parseInt(scanner.nextLine()) - 1;
                        System.out.println("Choose what you want to change:");
                        System.out.println("1: Change title, 2: Change body, 3: Change tags, 4: Add tags.");
                        int change_choice = Integer.parseInt(scanner.nextLine());
                        switch (change_choice) {
                            case 1:
                                System.out.println("Enter new title:");
                                title = scanner.nextLine();
                                app.change_title(id, title);
                                break;
                            case 2:
                                System.out.println("Enter new body:");
                                body = scanner.nextLine();
                                app.change_body(id, body);
                                break;
                            case 3:
                                System.out.println("Enter new tags separated by spaces:");
                                tags = scanner.nextLine();
                                app.change_tags(id, tags);
                                break;
                            case 4:
                                System.out.println("Enter new tags separated by spaces:");
                                tags = scanner.nextLine();
                                app.add_tags(id, tags);
                                break;
                        }
                        break;
                    case 5:
                        System.out.println("Enter note number:");
                        id = Integer.parseInt(scanner.nextLine()) - 1;
                        if (app.is_deadline_setted(id)) {
                            System.out.println("Note already have deadline!");
                            System.out.println("Do you want to change it? y/n");
                            String confirm = scanner.nextLine().toLowerCase();
                            if (confirm.equals("y") || confirm.equals("yes")) {
                                System.out.println("Enter deadline in format dd/mm/yyyy:");
                                app.set_deadline(id, scanner.nextLine());
                            }
                        } else {
                            System.out.println("Enter deadline in format dd/mm/yyyy:");
                            app.set_deadline(id, scanner.nextLine());
                        }
                        break;
                    case 6:
                        System.out.println("Choose sorting type");
                        System.out.println("1: By title, 2: By creation date.");
                        int sort_choice = Integer.parseInt(scanner.nextLine());
                        switch (sort_choice) {
                            case 1:
                                app.sort_ByTitle();
                                break;
                            case 2:
                                app.sort_ByCreationDate();
                                break;
                        }
                        break;
                    case 7:
                        System.out.println("Choose type of search");
                        System.out.println("1: By title, 2: By tags.");
                        int search_choice = Integer.parseInt(scanner.nextLine());
                        switch (search_choice) {
                            case 1:
                                System.out.println("Enter title:");
                                title = scanner.nextLine();
                                app.search_ByTitle(title);
                                break;
                            case 2:
                                System.out.println("Enter tags separated by spaces:");
                                tags = scanner.nextLine();
                                app.search_ByTags(tags);
                                break;
                        }
                        break;
                    case 8:
                        app.save_notes();
                        exit = true;
                        break;
                }
                System.out.println("-----------------------------------------------------------------");
            }
            catch (NumberFormatException e){
                System.err.println("Error: not a number entered");
            }
        }
    }
}