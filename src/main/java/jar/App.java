package jar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        var filePath = "src/main/resources/bank-statement.csv";
        List<String> strings = Files.readAllLines(Path.of(filePath));
        System.out.println( strings );
        double total = 0;
        Map<Month, Double> totalByMonth= new HashMap<>();
        Map<String, Double> totalByCategory= new TreeMap<>();

        // calculando o total de despesas
        for (String linha : strings) {
            String[] transaction = linha.split(",");
            //converter string para data
            LocalDate date = LocalDate.parse(transaction[0], DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            //adicionar o mes na lista de gastos por mes
            double value = Double.parseDouble(transaction[1]);
            String category = transaction[2];

            Double previousValue = totalByMonth.get(date.getMonth()) ;
            if (previousValue !=null) {
                totalByMonth.put(date.getMonth(), previousValue + value);
            }else{
                totalByMonth.put(date.getMonth(), value);
            }

            if (value<=0) {
                Double previousCategoryValue = totalByCategory.get(category);
                if (previousCategoryValue != null) {
                    totalByCategory.put(category, previousCategoryValue + value);
                } else {
                    totalByCategory.put(category, value);
                }
            }





            total+=Double.parseDouble(transaction[1]);
        }

        Map<String, Double> map = totalByCategory.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors
                .toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        System.out.println("Qual é'o lucro total do extrato bancario?" + total);
        System.out.println("Quantas transacoes foram feitas no mes de Janeiro?" + totalByMonth.get(Month.JANUARY));
        System.out.println("Quais sao 10 maiores despesas? " + map);
        System.out.println("Em qual categoria, o gasto de dinheiro foi maior? " + map.entrySet().stream().findFirst().get());

    }
}
