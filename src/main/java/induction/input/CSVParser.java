package induction.input;


import induction.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class CSVParser implements Parser {
    private final static Logger logger = LoggerFactory.getLogger(CSVParser.class);

    private List<List<String>> rows = new LinkedList<>();
    private List<String> columns = new LinkedList<>();
    private List<String> conditions = new LinkedList<>(); //if a condition is met, the row is rejected
    private List<Pattern> patterns = new LinkedList<>();
    int minlength = 1;
    String separator = ",";


    public CSVParser() {
    }

    public CSVParser(List<String> conditions) {
        this.conditions = conditions;

        for (String temp : conditions) {
            patterns.add(Pattern.compile(temp));
        }
    }


    public static void main(String[] args) {

        CSVParser obj = new CSVParser();
        obj.getRows();

    }

    public void parse(File file) {
        try {
            this.parse(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void parse(Reader reader) {
        BufferedReader br = null;
        String line = "";
        try {
            int counter = 0;
            br = new BufferedReader(reader);
            while ((line = br.readLine()) != null) {

                List<String> row = new LinkedList<>();
                String[] cells = line.split(separator);

                boolean rowValidity = true;


                for (int i = 0; i < cells.length && rowValidity == true; i++) {
                    cells[i] = cells[i].replaceAll("\t", "");
                    if (cells[i].trim().length() < minlength) {
                        rowValidity = false;
                        logger.info("\"" + cells[i] + "\"" + " length < minlength ");
                        break;
                    } else {
                        for (Pattern temp : patterns) {
                            rowValidity = !temp.matcher((cells[i])).find();
                            if (!rowValidity) {
                                logger.info("\"" + cells[i] + "\"" + " match found " + temp);
                            }
                        }
                    }
                }

                if (!rowValidity) {
                    continue;
                }

                row.addAll(Arrays.asList(cells));


                if (counter == 0) {
                    columns.addAll(row);
                } else {
                    rows.add(row);
                }

                counter++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("CSV read Done");

    }

    public List<List<String>> getRows() {
        return rows;
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<Column> getParsedColumns() {
        return null;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public void addCondition(String regex) {

        this.conditions.add(regex);

        patterns.add(Pattern.compile(regex));
    }

    public void removeColumn(int columnNumber) {
        columns.remove(columnNumber);
        for (List<String> temp : rows) {
            temp.remove(columnNumber);
        }
    }

    public void setMinlength(int number) {
        this.minlength = number;
    }

    public void setConditions(List<String> conditions) {

        this.conditions = conditions;

        for (String temp : conditions) {
            patterns.add(Pattern.compile(temp));
        }

    }

    public void skipColumn(int column) {
        columns.remove(column);
        for (List<String> singleRow : rows) {
            if (singleRow.size() >= column) {
                singleRow.remove(column);
            } else {
                System.out.println("Cannot skip!");
                System.out.println(singleRow);
            }
        }
    }

}