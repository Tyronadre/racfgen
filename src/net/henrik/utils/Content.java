package net.henrik.utils;

import java.io.*;
import java.util.*;

public class Content {
    List<Data> contentList;
    File file_all = new File("src/contentList.txt");
    String lastCreated;

    public Content() {
        contentList = new ArrayList<>();
        try {
            if (file_all.createNewFile()) {
                System.err.println("WARNING! FILE NOT FOUND! CREATED NEW FILE!");
            }
        } catch (IOException e) {
            System.err.println(e + "ERROR CREATING FILE!");
        }
        read();
    }

    public String[] getTitle() {
        return new String[]{"RACF", "NAME"};
    }

    public String[][] getContent() {
        //UPDATE INTERNAL
        read();
        //CHECK IF CONTENT IS AVAILABLE
        if (contentList.isEmpty()) {
            System.err.println("WARNING: NO CONTENT FOUND");
            return new String[][]{{"NO CONTENT", "NO CONTENT"}};
        }
        //GET CONTENT
        String[][] content = new String[contentList.size()][2];
        for (int i = 0; i < contentList.size(); i++) {
            content[i][0] = contentList.get(i).racf;
            content[i][1] = contentList.get(i).name;
        }
        return content;
    }

    private void read() {
        try (RACFReader reader = new RACFReader(file_all)) {
            String[] temp;
            contentList.clear();
            while ((temp = reader.readMyLine()) != null) {
                contentList.add(new Data(temp[0], temp[1], temp[2]));
            }
            contentList.sort(Comparator.comparing(o -> o.racf));
        } catch (IOException e) {
            System.err.println("File_ACTUAL Read Error");
        }
    }

    public String add(String name) {
        String racf = parseRACF(name);
        read();
        try (RACFWriter writer = new RACFWriter(file_all)) {
            String id = getID();
            writer.writeMyLine(id, racf, name);

            contentList.add(new Data(id, racf, name));
            lastCreated = id;
        } catch (IOException e) {
            System.err.println("File Write Error");
        }
        return racf;
    }

    private String getID() {
        String id = UUID.randomUUID().toString();
        List<String> ids = new ArrayList<>();
        contentList.stream().map(data -> data.id).forEach(ids::add);
        while (ids.contains(id))
            id = UUID.randomUUID().toString();
        return id;
    }

    private String parseRACF(String name) {
        read();
        String racf_name = parseName(name);
        int racf_num_temp = 1;
        List<Integer> num_list = new LinkedList<>();
        for (Data data : contentList) {
            if (data.racf.length() >= 4 && racf_name.equals(data.racf.substring(0, 4))) {
                try {
                    num_list.add(Integer.parseInt(data.racf.substring(5, 7)));
                } catch (Exception e) {
                    System.err.println(("Couldn't parse "+ data.racf+ ". RACF will be ignored!"));
                }
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
            throw new IllegalArgumentException("Not a name!");
        for (char c : name.toCharArray())
            if (!(Character.isLetter(c) || c == 'ß' || c == ' '))
                throw new IllegalArgumentException("Not a name!");


        String[] temp = name.split(" ");
        String lastname = temp[temp.length - 1];
        StringBuilder racfname = new StringBuilder();
        while (racfname.length() < 4) {
            //Wenn nachname kürzer als 4
            if (lastname.length() == 0) {
                lastname = temp[0];
            }
            //Fälle für ä,ö,ü
            switch (lastname.charAt(0)) {
                case 'ä':
                case 'Ä':
                    lastname = lastname.substring(1);
                    racfname.append("ae");
                    break;
                case 'ö':
                case 'Ö':
                    lastname = lastname.substring(1);
                    racfname.append("oe");
                    break;
                case 'ü':
                case 'Ü':
                    lastname = lastname.substring(1);
                    racfname.append("ue");
                    break;
                case 'É':
                case 'È':
                case 'é':
                case 'è':
                    lastname = lastname.substring(1);
                    racfname.append("e");
                    break;
                case 'ß':
                    lastname = lastname.substring(1);
                    racfname.append("ss");
                    break;
                default:
                    if (lastname.startsWith("sch")) {
                        racfname.append("sc");
                        lastname = lastname.substring(3);
                    } else {
                        racfname.append(lastname.charAt(0));
                        lastname = lastname.substring(1);
                    }
            }

        }
        return racfname.substring(0, 4).toLowerCase();
    }

    public String deleteLast() throws IOException {
        String name = "";
        Data d = null;
        for (Data data : contentList) {
            if (data.id.equals(lastCreated)) {
                name = data.name;
                d = data;
                break;
            }
        }
        contentList.remove(d);
        if (!file_all.delete())
            System.err.println("File Write Error");
        if (!file_all.createNewFile())
            System.err.println("File Write Error");
        try (RACFWriter writer = new RACFWriter(file_all)) {
            for (Data t : contentList) {
                writer.writeMyLine(t.id, t.racf, t.name);
            }
        } catch (IOException e) {
            System.err.println("File Write Error");
        }
        return name;
    }

    public void importCSV() throws IOException {
        File csv = new File("src/export.csv");
        if (!file_all.delete())
            throw new IOException("Failed to Import");
        if (!file_all.createNewFile())
            throw new IOException("Failed to Import");
        try (BufferedReader reader = new BufferedReader(new FileReader("src/export.csv")); RACFWriter writer = new RACFWriter(file_all)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] t = line.split(",");
                writer.writeMyLine(getID(), t[0], t[1] + " " + t[2]);
            }
        } catch (IOException e) {
            throw new IOException("Failed to Import");
        }

    }

    static class Data {
        String id;
        String racf;
        String name;

        Data(String id, String racf, String name) {
            this.id = id;
            this.racf = racf;
            this.name = name;
        }
    }
}
