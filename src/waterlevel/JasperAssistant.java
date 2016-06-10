package waterlevel;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.List;

/**
 * Class: JasperAssistant
 * Version: 0.3
 * Created on 14.05.2016 with the help of IntelliJ IDEA (thanks!)
 * Author: Evnica
 * Description: Creates a data source for jasper report (table model format) and compiles report blueprint
 */

public class JasperAssistant
{

    private static List<DayMeasurement> measurements;
    private static int numberOfRows = 0;
    private static Object[][] tableContent;
    private static TableModel tableModel;

    public static void init (List<DayMeasurement> chosenMeasurements)
    {
        measurements = chosenMeasurements;
        createTableModel();
    }

    public static TableModel getTableModel()
    {
        return tableModel;
    }

    private static void createTableModel()
    {
        filInTableContent();
        String columns[] = {"date", "timestamp", "value"};
        tableModel = new DefaultTableModel( tableContent, columns );
    }

    private static void filInTableContent( )
    {
        countSizeOfData();
        if ( numberOfRows > 0 )
        {
            tableContent = new Object[numberOfRows][3];
            String date;
            int i = 0;

            for (DayMeasurement day: measurements )
            {
                date = Formatter.getDATE_FORMATTER_ddMMyyyy().format( day.date);
                for (Measurement pair: day.hourlyMeasurementValues)
                {
                    tableContent[i][0] = date;
                    tableContent[i][1] = Formatter.getTIME_FORMATTER_HHmm().format(pair.timestamp);
                    tableContent[i][2] = pair.value;
                    i++;
                }
            }
        }
    }

    public static int countSizeOfData()
    {
        numberOfRows = 0;
        if ( measurements != null && measurements.size() > 0 )
        {
            for (DayMeasurement day: measurements )
            {
                numberOfRows = numberOfRows + day.hourlyMeasurementValues.size();
            }
        }
        return numberOfRows;
    }


}
