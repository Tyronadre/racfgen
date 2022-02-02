package net.henrik.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Content {
    List<String> racf;
    List<String> names;
    File file_racf = new File("racf.txt");
    File file_name = new File("names.txt");
    int lastCreated;

    public Content() {
        racf = new ArrayList<>();
        names = new ArrayList<>();
        read();
    }

    public String[] getTitle() {
        return new String[]{"RACF", "NAME"};
    }

    public String[][] getContent() {
        read();
        if (racf.isEmpty()) {
            System.err.println("WARNING: NO CONTENT FOUND");
            return new String[][]{{"NO CONTENT", "NO CONTENT"}};
        }
        String[][] content = new String[racf.size()][2];
        for (int i = 0; i < racf.size(); i++) {
            content[i][0] = racf.get(i);
            content[i][1] = names.get(i);
        }
        return content;
    }

    private void read() {
        try {
            if (file_name.createNewFile() | file_racf.createNewFile()) {
                System.err.println("WARNING! FILE NOT FOUND! CREATED NEW FILE!");
            }
        } catch (IOException e) {
            System.err.println(e + "ERROR CREATING FILE!");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file_racf)); BufferedReader reader2 = new BufferedReader(new FileReader(file_name))) {
            String line;
            while ((line = reader.readLine()) != null) {
                racf.clear();
                names.clear();
                racf.add(line);
                names.add(reader2.readLine());
            }
        } catch (IOException e) {
            System.err.println("File Read Error");
        }
    }

    public void add(String name) {
        System.out.println("adding" + name);
        read();
        String racf = parseRACF(name);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file_racf, true)); BufferedWriter writer2 = new BufferedWriter(new FileWriter(file_name, true))) {
            writer.write(racf);
            writer2.write(name);
            writer.newLine();
            writer2.newLine();
            this.racf.add(racf);
            this.names.add(name);
            lastCreated = this.names.size()-1;
        } catch (IOException e) {
            System.err.println("File Write Error");
        }
    }

    @Override
    public String toString() {
        return "Content{" + Arrays.deepToString(getContent()) + "}, Title{" + Arrays.toString(getTitle());
    }

    private String parseRACF(String name) {
        String racf_name = parseName(name);
        int racf_num_temp = 1;
        List<Integer> num_list = new LinkedList<>();
        for (String racf : this.racf) {
            if (racf_name.equals(racf.substring(0, 4))) {
                num_list.add(Integer.parseInt(racf.substring(5, 7)));
            }
        }
        while (num_list.contains(racf_num_temp))
            racf_num_temp++;
        StringBuilder racf_num = new StringBuilder(String.valueOf(racf_num_temp));
        while (racf_num.length() < 3)
            racf_num.insert(0, "0");
        return racf_name + racf_num;
    }

    private String parseName(String name) {
        if (!name.contains(" "))
            throw new IllegalArgumentException();
        String lastname = name.split(" ")[1];
        StringBuilder racfname = new StringBuilder();
        while (racfname.length() < 4) {
            //Wenn nachname kürzer als 4
            if (lastname.length() == 0) {
                lastname = name.split(" ")[0];
            }
            //Fälle für ä,ö,ü
            if (lastname.charAt(0) == 'ä')
                racfname.append("ae");
            else if (lastname.charAt(0) == 'ö')
                racfname.append("oe");
            else if (lastname.charAt(0) == 'ü')
                racfname.append("ue");
                //Fall sch
            else if (lastname.startsWith("sch")) {
                racfname.append("sc");
                lastname = lastname.substring(3);
                continue;
            } else
                racfname.append(lastname.charAt(0));
            lastname = lastname.substring(1);
        }
        return racfname.toString();
    }

    public void deleteLast(){
        this.racf.remove(lastCreated);
        this.names.remove(lastCreated);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file_racf, false)); BufferedWriter writer2 = new BufferedWriter(new FileWriter(file_name, true))) {
            file_name.delete();
            file_racf.delete();
            file_name.createNewFile();
            file_racf.createNewFile();
            for (int i = 0; i < racf.size(); i++) {
                writer.write(racf.get(i));
                writer.newLine();
                writer2.write(names.get(i));
                writer2.newLine();
            }
        } catch (IOException e) {
            System.err.println("File Write Error");
        }
    }

    public void delete(String racf){
        if (!this.racf.contains(racf))
            throw new IllegalArgumentException();
        names.remove(this.racf.indexOf(racf));
        this.racf.remove(racf);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file_racf, false)); BufferedWriter writer2 = new BufferedWriter(new FileWriter(file_name, true))) {
            file_name.delete();
            file_racf.delete();
            file_name.createNewFile();
            file_racf.createNewFile();
            for (int i = 0; i < this.racf.size(); i++) {
                writer.write(this.racf.get(i));
                writer.newLine();
                writer2.write(names.get(i));
                writer2.newLine();
            }
        } catch (IOException e) {
            System.err.println("File Write Error");
        }
    }

}
