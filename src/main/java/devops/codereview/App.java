package devops.codereview;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class App
{
    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public void connect(String location, int delay) {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                // Wait a bit for db to start
                Thread.sleep(delay);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://" + location
                                + "/world?allowPublicKeyRetrieval=true&useSSL=false",
                        "root", "Team_4");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect()
    {
        if (con != null)
        {
            try
            {
                // Close connection
                con.close();
            }
            catch (Exception e)
            {
                System.out.println("Error closing connection to database");
            }
        }
    }

    /**
     * 1. All the countries in the world organised by largest population to smallest.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<Country> getAllCountries()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 1: All the countries in the world organised by largest population to smallest.
            String strQueryOne =
                    "SELECT country.Code, country.Name as 'CountryName', country.Continent, country.Region, city.Name as 'CityName', country.Population  FROM country INNER JOIN city WHERE country.Code = city.CountryCode ORDER BY country.Population DESC;";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryOne);
            // Extract countries information
            ArrayList<Country> countries = new ArrayList<Country>();
            while (rset.next())
            {
                Country ctr = new Country();
                ctr.setCountryCode(rset.getString("Code"));
                ctr.setCountryName(rset.getString("CountryName"));
                ctr.setCountryCont(rset.getString("Continent"));
                ctr.setCountryReg(rset.getString("Region"));
                ctr.setCountryPopulation(rset.getString("Population"));
                ctr.setCountryCap(rset.getString("CityName"));
                countries.add(ctr);
            }
            return countries;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get all the countries in the world organised by largest population to smallest.");
            return null;
        }
    }
    /**
     * 1. All the countries in the world organised by largest population to smallest.
     * Formatting the output data from the list.
     **/
    public void printAllCountries(ArrayList<Country> countries)
    {
        // Check countries is not null
        if (countries == null)
        {
            System.out.println("No Countries");
            return;
        }
        // Print header
        System.out.println(String.format("%-10s %-40s %-30s %-30s %-30s %-20s", "Code","Name","Continent","Region","Population","Capital"));
        // Loop over all countries in the list
        for (Country c : countries)
        {
            if (c == null)
                continue;
            String allctrString =
                    String.format("%-10s %-40s %-30s %-30s %-30s %-20s",
                            c.getCountryCode(),c.getCountryName(),c.getCountryCont(),c.getCountryReg(),c.getCountryPopulation(),c.getCountryCap());
            System.out.println(allctrString);
        }
    }
    /**
     * Outputs to Markdown
     * 1. All the countries in the world organised by largest population to smallest.
     * @param countries
     */
    public void outputCountries(ArrayList<Country> countries, String AllCountries) {
        // Check countries is not null
        if (countries == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int i = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | Country Code | Country Name | Continent | Region | Population | Capital |\r\n");
        sb.append("| --- | --- | --- | --- | --- | --- | --- |\r\n");
        // Loop over all countries in the list
        for (Country cou : countries) {
            i += 1;
            if (cou == null) continue;
            sb.append("| " + i + "| " + cou.getCountryCode() + " | " +
                    cou.getCountryName() + " | " + cou.getCountryCont() + " | " +
                    cou.getCountryReg() + " | " + numFormat.format(Integer.parseInt(cou.getCountryPopulation())) + " | "
                    + cou.getCountryCap() + "|\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + AllCountries)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 2. All the countries in a continent organised by largest population to smallest.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<Country> getAllContinents(String inputContinent)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 2: All the countries in a continent organised by largest population to smallest.
            String strQueryTwo =
                    "SELECT country.Code, country.Name as 'CountryName', country.Continent, country.Region, city.Name as 'CityName', country.Population FROM country INNER JOIN city WHERE country.Code = city.CountryCode AND country.Capital=city.ID AND country.Continent = '"+inputContinent+"' ORDER BY country.Population DESC;";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryTwo);
            // Extract continent information
            ArrayList<Country> continent = new ArrayList<Country>();
            while (rset.next())
            {
                Country contnt = new Country();
                contnt.setCountryCode(rset.getString("Code"));
                contnt.setCountryName(rset.getString("CountryName"));
                contnt.setCountryCont(rset.getString("Continent"));
                contnt.setCountryReg(rset.getString("Region"));
                contnt.setCountryPopulation(rset.getString("Population"));
                contnt.setCountryCap(rset.getString("CityName"));
                continent.add(contnt);
            }
            return continent;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get all the countries in Oceania organised by largest population to smallest.");
            return null;
        }
    }

    /**
     * 2. All the countries in a continent organised by largest population to smallest.
     *
     Su Hnin, [12/25/2022 11:38 PM]
     ormatting the output data from the list.
     **/
    public void printContinent(ArrayList<Country> continent)
    {
        // Check continent is not null
        if (continent == null)
        {
            System.out.println("There is no country in Oceania.");
            return;
        }
        // Print header
        System.out.println(String.format("%-10s %-40s %-30s %-30s %-30s %-30s","Code","Country Name","Continent","Region","Population","Capital"));
        // Loop over all continent in the list
        for (Country cont : continent)
        {
            if (cont == null)
                continue;
            String contString =
                    String.format("%-10s %-40s %-30s %-30s %-30s %-30s",
                            cont.getCountryCode(),cont.getCountryName(),cont.getCountryCont(),cont.getCountryReg(),cont.getCountryPopulation(),cont.getCountryCap());
            System.out.println(contString);
        }
    }
    /**
     * Outputs to Markdown
     * 2. All the countries in a continent organised by largest population to smallest.
     * @param continents
     */
    public void outputContinent(ArrayList<Country> continents, String AllContinents) {
        // Check continent is not null
        if (continents == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int i = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | Country Code | Country Name | Continent | Region | Population | Capital |\r\n");
        sb.append("| --- | --- | --- | --- | --- | --- | --- |\r\n");
        // Loop over all continent in the list
        for (Country cont : continents) {
            i += 1;
            if (cont == null) continue;
            sb.append("| " + i + "| " + cont.getCountryCode() + " | " +
                    cont.getCountryName() + " | " + cont.getCountryCont() + " | " +
                    cont.getCountryReg() + " | " + numFormat.format(Integer.parseInt(cont.getCountryPopulation())) + " | "
                    + cont.getCountryCap() + "|\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + AllContinents)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 3. All the countries in a region organised by largest population to smallest.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<Country> getAllRegion(String inputRegion)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 3: All the countries in a region organised by largest population to smallest.
            String strQueryThree =
                    "SELECT country.Code, country.Name as 'CountryName', country.Continent, country.Region, city.Name as 'CityName', country.Population FROM country INNER JOIN city WHERE country.Code = city.CountryCode AND country.Capital=city.ID AND country.Region = '"+inputRegion+"' ORDER BY country.Population DESC;";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryThree);
            // Extract country information
            ArrayList<Country> regions = new ArrayList<Country>();
            while (rset.next())
            {
                Country reg           = new Country();
                reg.setCountryCode(rset.getString("Code"));
                reg.setCountryName(rset.getString("CountryName"));
                reg.setCountryCont(rset.getString("Continent"));
                reg.setCountryReg(rset.getString("Region"));
                reg.setCountryPopulation(rset.getString("Population"));
                reg.setCountryCap(rset.getString("CityName"));
                regions.add(reg);
            }
            return regions;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get all the countries in Caribbean organised by largest population to smallest.");
            return null;
        }
    }
    /**
     * 3. All the countries in a region organised by largest population to smallest.
     * Formatting the output data from the list.
     **/
    public void printRegion(ArrayList<Country> region)
    {
        // Check region is not null
        if (region == null)
        {
            System.out.println("There is no region in Caribbean.");
            return;
        }
        // Print header
        System.out.println(String.format("%-10s %-40s %-30s %-30s %-30s %-30s", "Code","Country Name","Continent","Region","Population","Capital"));
        // Loop over all region in the list
        for (Country r : region)
        {
            //printRegion to check if an region is null
            if (r == null)
                continue;
            String regString =
                    String.format("%-10s %-40s %-30s %-30s %-30s %-30s",
                            r.getCountryCode(),r.getCountryName(),r.getCountryCont(),r.getCountryReg(),r.getCountryPopulation(),r.getCountryCap());
            System.out.println(regString);
        }
    }
    /**
     * Outputs to Markdown
     * 3. All the countries in a region organised by largest population to smallest.
     * @param allregion
     */
    public void outputRegion(ArrayList<Country> allregion, String AllRegions) {
        // Check country is not null
        if (allregion == null)
        {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int i = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | Country Code | Country Name | Continent | Region | Population | Capital |\r\n");
        sb.append("| --- | --- | --- | --- | --- | --- | --- |\r\n");
        // Loop over all regions in the list
        for (Country regs : allregion)
        {
            i += 1;
            if (regs == null) continue;
            sb.append("| " + i + "| " + regs.getCountryCode() + " | " +
                    regs.getCountryName() + " | " + regs.getCountryCont() + " | " +
                    regs.getCountryReg() + " | " + numFormat.format(Integer.parseInt(regs.getCountryPopulation())) + " | "
                    + regs.getCountryCap() + "|\r\n");
        }
        try
        {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + AllRegions)));
            writer.write(sb.toString());
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * 4. The top N populated countries in the world where N is provided by the user.
     * Query execution by user input and pass the array list to format the return value.
     * Function is called in ma
     **/
    public ArrayList<Country> getAllNPopulatedCountries(int inputLimit)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 4: The top N populated countries in the world where N is provided by the user.
            String strQueryFour =
                    "SELECT country.Code, country.Name as 'CountryName', country.Continent, country.Region, city.Name as 'CityName',country.Population FROM country INNER JOIN city WHERE country.Code = city.CountryCode AND country.Capital=city.ID ORDER BY country.Population DESC LIMIT "+inputLimit+";";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryFour);
            // Extract countries information
            ArrayList<Country> nPopulatedCountry = new ArrayList<Country>();
            while (rset.next())
            {
                Country npopctr = new Country();
                npopctr.setCountryCode(rset.getString("Code"));
                npopctr.setCountryName(rset.getString("CountryName"));
                npopctr.setCountryCont(rset.getString("Continent"));
                npopctr.setCountryReg(rset.getString("Region"));
                npopctr.setCountryPopulation(rset.getString("Population"));
                npopctr.setCountryCap(rset.getString("CityName"));
                nPopulatedCountry.add(npopctr);
            }
            return nPopulatedCountry;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the top 10 populated countries in the world.");
            return null;
        }
    }

    /**
     * 4. The top N populated countries in the world where N is provided by the user.
     * Formatting the output data from the list.
     **/
    public void printNPopulatedCountries(ArrayList<Country> nPopulatedCountry)
    {
        // Check npopulatedcountries is not null
        if (nPopulatedCountry == null)
        {
            System.out.println("There is no top 10 populated country in the world.");
            return;
        }
        // Print header
        System.out.println(String.format("%-10s %-40s %-30s %-30s %-30s %-30s", "Code", "Country Name", "Continent", "Region", "Population", "Capital"));
        // Loop over top n country in the list
        for (Country npopc : nPopulatedCountry)
        {
            //printcontinent to check if an Continent is null
            if (npopc == null)
                continue;
            String npopctrString =
                    String.format("%-10s %-40s %-30s %-30s %-30s %-30s",
                            npopc.getCountryCode(), npopc.getCountryName(), npopc.getCountryCont(), npopc.getCountryReg(), npopc.getCountryPopulation(), npopc.getCountryCap());
            System.out.println(npopctrString);
        }
    }
    /**
     * Outputs to Markdown
     * 4. The top N populated countries in the world where N is provided by the user.
     * @param ncountries
     */
    public void outputNCountries(ArrayList<Country> ncountries, String TopNCountries) {
        // Check ncountries is not null
        if (ncountries == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int i = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | Country Code | Country Name | Continent | Region | Population | Capital |\r\n");
        sb.append("| --- | --- | --- | --- | --- | --- | --- |\r\n");
        // Loop over top n country in the list
        for (Country ncont : ncountries) {
            i += 1;
            if (ncont == null) continue;
            sb.append("| " + i + "| " + ncont.getCountryCode() + " | " +
                    ncont.getCountryName() + " | " + ncont.getCountryCont() + " | " +
                    ncont.getCountryReg() + " | " + numFormat.format(Integer.parseInt(ncont.getCountryPopulation())) + " | "
                    + ncont.getCountryCap() + "|\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + TopNCountries)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 5. The top N populated countries in a continent where N is provided by the user.
     * Query execution by user input and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<Country> getAllNPopulatedContinents(String inputTopCon, int inputLimit)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 5: The top N populated countries in a continent where N is provided by the user.
            String strQueryFive =
                    "SELECT country.Code, country.Name as 'CountryName', country.Continent, country.Region, city.Name as 'CityName', country.Population FROM country INNER JOIN city WHERE country.Code = city.CountryCode AND country.Capital=city.ID AND country.Continent = '"+ inputTopCon +"' ORDER BY country.Population DESC LIMIT "+ inputLimit +";";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryFive);
            // Extract country information
            ArrayList<Country> nPopulatedContinents = new ArrayList<Country>();
            while (rset.next())
            {
                Country npopcont = new Country();
                npopcont.setCountryCode(rset.getString("Code"));
                npopcont.setCountryName(rset.getString("CountryName"));
                npopcont.setCountryCont(rset.getString("Continent"));
                npopcont.setCountryReg(rset.getString("Region"));
                npopcont.setCountryPopulation(rset.getString("Population"));
                npopcont.setCountryCap(rset.getString("CityName"));
                nPopulatedContinents.add(npopcont);
            }
            return nPopulatedContinents;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the top 10 populated countries in Europe.");
            return null;
        }
    }
    /**
     * 5. The top N populated countries in a continent where N is provided by the user.
     * Query execution by user input and pass the array list to format the return value.
     * Function is called in main.
     **/
    public void printNPopulatedContinents(ArrayList<Country> nPopulatedContinents)
    {
        // Check npopulatedcontinent is not null
        if (nPopulatedContinents == null)
        {
            System.out.println("There is no top 10 populated country in Europe.");
            return;
        }
        // Print header
        System.out.println(String.format("%-10s %-40s %-30s %-30s %-30s %-30s","Code","Country Name","Continent","Region","Population","Capital"));
        // Loop over all continent in the list
        for (Country npopconti : nPopulatedContinents)
        {
            //printRegion to check if an Continent is null
            if (npopconti == null)
                continue;
            String npopcontString =
                    String.format("%-10s %-40s %-30s %-30s %-30s %-30s",
                            npopconti.getCountryCode(),npopconti.getCountryName(),npopconti.getCountryCont(),npopconti.getCountryReg(),npopconti.getCountryPopulation(),npopconti.getCountryCap());
            System.out.println(npopcontString);
        }
    }
    /**
     * Outputs to Markdown
     * 5. The top N populated countries in a continent where N is provided by the user.
     * @param ncontinents
     */
    public void outputNContinents(ArrayList<Country> ncontinents, String TopNContinents) {
        // Check countries is not null
        if (ncontinents == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int i = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | Country Code | Country Name | Continent | Region | Population | Capital |\r\n");
        sb.append("| --- | --- | --- | --- | --- | --- | --- |\r\n");
        // Loop over top n continent in the list
        for (Country ncont : ncontinents) {
            i += 1;
            if (ncont == null) continue;
            sb.append("| " + i + "| " + ncont.getCountryCode() + " | " +
                    ncont.getCountryName() + " | " + ncont.getCountryCont() + " | " +
                    ncont.getCountryReg() + " | " + numFormat.format(Integer.parseInt(ncont.getCountryPopulation())) + " | "
                    + ncont.getCountryCap() + "|\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + TopNContinents)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 6. The top N populated countries in a region where N is provided by the user.
     * Query execution by user input and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<Country> getAllNPopulatedRegion(String inputTopReg, int inputLimit)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 6: The top N populated countries in a region where N is provided by the user.
            String strQuerySix =
                    "SELECT country.Code, country.Name as 'CountryName', country.Continent, country.Region, city.Name as 'CityName', country.Population FROM country INNER JOIN city WHERE country.Code = city.CountryCode AND country.Capital=city.ID AND country.Region = '"+ inputTopReg +"' ORDER BY country.Population DESC LIMIT "+ inputLimit +";";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQuerySix);
            // Extract country information
            ArrayList<Country> nPopulatedRegion = new ArrayList<Country>();
            while (rset.next())
            {
                Country npopreg           = new Country();
                npopreg.setCountryCode(rset.getString("Code"));
                npopreg.setCountryName(rset.getString("CountryName"));
                npopreg.setCountryCont(rset.getString("Continent"));
                npopreg.setCountryReg(rset.getString("Region"));
                npopreg.setCountryPopulation(rset.getString("Population"));
                npopreg.setCountryCap(rset.getString("CityName"));
                nPopulatedRegion.add(npopreg);
            }
            return nPopulatedRegion;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the top 10 populated countries in Caribbean.");
            return null;
        }
    }
    /**
     * 6. The top N populated countries in a region where N is provided by the user
     * Query execution by user input and pass the array list to format the return value.
     * Function is called in main.
     **/
    public void printNPopulatedRegion(ArrayList<Country> nPopulatedRegion)
    {
        // Check npopulatedregion is not null
        if (nPopulatedRegion == null)
        {
            System.out.println("There is no top 10 populated country in Caribbean.");
            return;
        }
        // Print header
        System.out.println(String.format("%-10s %-40s %-30s %-30s %-30s %-30s","Code","Country Name","Continent","Region","Population","Capital"));
        // Loop over all region in the list
        for (Country npopreg : nPopulatedRegion)
        {
            //printNpopulatedRegion to check if an region is null
            if (npopreg == null)
                continue;
            String npopregString =
                    String.format("%-10s %-40s %-30s %-30s %-30s %-30s",
                            npopreg.getCountryCode(),npopreg.getCountryName(),npopreg.getCountryCont(),npopreg.getCountryReg(),npopreg.getCountryPopulation(),npopreg.getCountryCap());
            System.out.println(npopregString);
        }
    }
    /**
     * Outputs to Markdown
     * 6. The top N populated countries in a region where N is provided by the user.
     * @param nregions
     */
    public void outputNRegions(ArrayList<Country> nregions, String TopNRegions) {
        // Check ncontinents is not null
        if (nregions == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int i = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | Country Code | Country Name | Continent | Region | Population | Capital |\r\n");
        sb.append("| --- | --- | --- | --- | --- | --- | --- |\r\n");
        // Loop over top n region in the list
        for (Country nreg : nregions) {
            i += 1;
            if (nreg == null) continue;
            sb.append("| " + i + "| " + nreg.getCountryCode() + " | " +
                    nreg.getCountryName() + " | " + nreg.getCountryCont() + " | " +
                    nreg.getCountryReg() + " | " + numFormat.format(Integer.parseInt(nreg.getCountryPopulation())) + " | "
                    + nreg.getCountryCap() + "|\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + TopNRegions)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 7. All the cities in the world organised by largest population to smallest.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<City> getAllCities()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 7: All the cities in the world organised by largest population to smallest.
            String strQuerySeven =
                    "SELECT city.Name as 'CityName',country.Name as 'CountryName',city.District,city.Population FROM city INNER JOIN country WHERE country.Code = city.CountryCode ORDER BY city.Population DESC;";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQuerySeven);
            // Extract cities information
            ArrayList<City> cities = new ArrayList<City>();
            while (rset.next())
            {
                City cit = new City();
                cit.setCitName(rset.getString("CityName"));
                cit.setCountryName(rset.getString("CountryName"));
                cit.setCitDistrict(rset.getString("District"));
                cit.setCitPopulation(rset.getString("Population"));
                cities.add(cit);
            }
            return cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get all the cities in the world organised by largest population to smallest.");
            return null;
        }
    }
    /**
     * 7. All the cities in the world organised by largest population to smallest.
     * Formatting the output data from the list.
     **/
    public void printCities(ArrayList<City> cities)
    {
        if (cities == null){
            System.out.println("There is no city in the world!");
        }
        //System.out.println("7. All the cities in the world organised by largest population to smallest.");
        // Print header
        System.out.println(String.format("%-35s %-40s %-25s %-25s","City Name","Country Name","District","Population"));
        // Loop over all cities in the list
        for (City cit : cities)
        {
            if (cit == null)
                continue;
            String allcitString =
                    String.format("%-35s %-40s %-25s %-25s",
                            cit.getCitName(),cit.getCountryName(),cit.getCitDistrict(),cit.getCitPopulation());
            System.out.println(allcitString);
        }
    }
    /**
     * Outputs to Markdown
     * 7. All the cities in the world organised by largest population to smallest.
     * @param allCity
     */
    public void outputCities(ArrayList<City> allCity, String allCities) {
        // Check cities is not null
        if (allCity == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int id = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | City Name | Country Name | District | Population |\r\n");
        sb.append("| --- | --- | --- | --- | --- |\r\n");
        // Loop over all cities in the list
        for (City cit : allCity) {
            id += 1;
            if (cit == null) continue;
            sb.append("| " + id + "| " + cit.getCitName() + " | " +
                    cit.getCountryName() + " | " +
                    cit.getCitDistrict() + " | " + numFormat.format(Integer.parseInt(cit.getCitPopulation())) + " |\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + allCities)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 8. All the cities in a continent organised by largest population to smallest.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<City> getAllCitiesContinent(String inputCitContinent)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 8: All the cities in a continent organised by largest population to smallest.
            String strQueryEight =
                    "SELECT city.Name as 'CityName', country.Name as 'CountryName', city.District, country.Continent, city.Population FROM city INNER JOIN country on city.CountryCode = country.Code WHERE country.Continent = '"+ inputCitContinent +"' ORDER BY city.Population DESC;";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryEight);
            // Extract continent information
            ArrayList<City> continent = new ArrayList<City>();
            while (rset.next())
            {
                City conti = new City();
                conti.setCitName(rset.getString("CityName"));
                conti.setCountryName(rset.getString("CountryName"));
                conti.setCitDistrict(rset.getString("District"));
                conti.setCitCont(rset.getString("Continent"));
                conti.setCitPopulation(rset.getString("Population"));
                continent.add(conti);
            }
            return continent;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get all the cities in Asia organised by largest population to smallest.");
            return null;
        }
    }

    /**
     * 8. All the cities in a continent organised by largest population to smallest.
     * Formatting the output data from the list.
     **/
    public void printContinents(ArrayList<City> continent)
    {
        if (continent == null){
            System.out.println("There is no city in Asia.");
        }
        //System.out.println("8. All the cities in a continent organised by largest population to smallest.");
        // Print header
        System.out.println(String.format("%-35s %-40s %-25s %-25s %-25s","City Name","Country Name","District","Continent","Population"));
        // Loop over all cities in the list
        for (City c : continent)
        {
            if (c == null)
                continue;
            String allcontiString =
                    String.format("%-35s %-40s %-25s %-25s %-25s",
                            c.getCitName(),c.getCountryName(),c.getCitDistrict(),c.getCitCont(),c.getCitPopulation());
            System.out.println(allcontiString);
        }
    }
    /**
     * Outputs to Markdown
     * 8. All the cities in a continent organised by largest population to smallest.
     * @param continent
     */
    public void outputcitycontinent(ArrayList<City> continent, String cont) {
        // Check cities is not null
        if (continent == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int id = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | City Name | Country Name | District | Continent |Population |\r\n");
        sb.append("| --- | --- | --- | --- | --- | --- |\r\n");
        // Loop over all regions in the list
        for (City contpop : continent) {
            id += 1;
            if (contpop == null) continue;
            sb.append("| " + id + "| " + contpop.getCitName() + " | " +
                    contpop.getCountryName() + " | " + contpop.getCitDistrict() + " | " +
                    contpop.getCitCont() + " | " + numFormat.format(Integer.parseInt(contpop.getCitPopulation())) + " |\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + cont)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 9. All the cities in a region organised by largest population to smallest.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<City> getAllCitiesRegions(String inputCitRegions)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 9: All the cities in a region organised by largest population to smallest.
            String strQueryEight =
                    "SELECT city.Name as 'CityName',country.Name as 'CountryName', city.District, country.Region, city.Population FROM city INNER JOIN country on city.CountryCode = country.Code WHERE country.Region = '"+inputCitRegions+"' ORDER BY city.Population DESC;";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryEight);
            // Extract regions information
            ArrayList<City> regions = new ArrayList<City>();
            while (rset.next())
            {
                City reg          = new City();
                reg.setCitName(rset.getString("CityName"));
                reg.setCountryName(rset.getString("CountryName"));
                reg.setCitDistrict(rset.getString("District"));
                reg.setCitReg(rset.getString("Region"));
                reg.setCitPopulation(rset.getString("Population"));
                regions.add(reg);
            }
            return regions;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get all the cities in Caribbean organised by largest population to smallest.");
            return null;
        }
    }

    /**
     * 9. All the cities in a region organised by largest population to smallest.
     * Formatting the output data from the list.
     **/
    public void printRegions(ArrayList<City> regions)
    {
        if (regions == null){
            System.out.println("There is no city in Caribbean");
        }
        //System.out.println("9. All the cities in a region organised by largest population to smallest.");
        // Print header
        System.out.println(String.format("%-35s %-40s %-25s %-25s %-25s","City Name","Country Name","District","Region","Population"));
        // Loop over all cities in the list
        for (City r : regions)
        {
            if (r == null)
                continue;
            String allregString =
                    String.format("%-35s %-40s %-25s %-25s %-25s",
                            r.getCitName(),r.getCountryName(),r.getCitDistrict(),r.getCitReg(),r.getCitPopulation());
            System.out.println(allregString);
        }
    }
    /**
     * Outputs to Markdown
     * 9. All the cities in a region organised by largest population to smallest.
     * @param region
     */
    public void outputcityregion(ArrayList<City> region, String reg) {
        // Check cities is not null
        if (region == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int id = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | City Name | Country Name | District | Region |Population |\r\n");
        sb.append("| --- | --- | --- | --- | --- | --- |\r\n");
        // Loop over all regions in the list
        for (City citReg : region) {
            id += 1;
            if (citReg == null) continue;
            sb.append("| " + id + "| " + citReg.getCitName() + " | " +
                    citReg.getCountryName() + " | " + citReg.getCitDistrict() + " | " +
                    citReg.getCitReg() + " | " + numFormat.format(Integer.parseInt(citReg.getCitPopulation())) + " |\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + reg)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 10. All the cities in a country organised by largest population to smallest.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<City> getAllCitiesCountries(String inputCitCountries)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 10: All the cities in a country organised by largest population to smallest.
            String strQueryEight =
                    "SELECT city.Name as 'CityName',country.Name as 'CountryName',city.District, city.Population FROM country INNER JOIN city WHERE country.Code = city.CountryCode AND country.Name='"+inputCitCountries+"' ORDER BY city.Population DESC;";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryEight);
            // Extract country information
            ArrayList<City> countries = new ArrayList<City>();
            while (rset.next())
            {
                City c1           = new City();
                c1.setCitName(rset.getString("CityName"));
                c1.setCountryName(rset.getString("CountryName"));
                c1.setCitDistrict(rset.getString("District"));
                c1.setCitPopulation(rset.getString("Population"));
                countries.add(c1);
            }
            return countries;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get all the cities in Myanmar organised by largest population to smallest.");
            return null;
        }
    }

    /**
     * 10. All the cities in a country organised by largest population to smallest.
     * Formatting the output data from the list.
     **/
    public void printCountries(ArrayList<City> countries)
    {
        if (countries == null){
            System.out.println("There is no city in Myanmar.");
        }
        //System.out.println("10. All the cities in a country organised by largest population to smallest.");
        // Print header
        System.out.println(String.format("%-35s %-40s %-25s %-25s","City Name","Country Name","District","Population"));
        // Loop over all cities in the list
        for (City cou : countries)
        {
            if (cou == null)
                continue;
            String allcouString =
                    String.format("%-35s %-40s %-25s %-25s",
                            cou.getCitName(),cou.getCountryName(),cou.getCitDistrict(),cou.getCitPopulation());
            System.out.println(allcouString);
        }
    }
    /**
     * Outputs to Markdown
     * 10. All the cities in a country organised by largest population to smallest.
     * @param citCount
     */
    public void outputcityCountry(ArrayList<City> citCount, String country) {
        // Check cities is not null
        if (citCount == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int id = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | City Name | Country Name | District | Population |\r\n");
        sb.append("| --- | --- | --- | --- | --- |\r\n");
        // Loop over all country in the list
        for (City citCt : citCount) {
            id += 1;
            if (citCt == null) continue;
            sb.append("| " + id + "| " + citCt.getCitName() + " | " +
                    citCt.getCountryName() + " | " +
                    citCt.getCitDistrict() + " | " + numFormat.format(Integer.parseInt(citCt.getCitPopulation())) + " |\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + country)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 11. All the cities in a district organised by largest population to smallest.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<City> getAllCitiesDistrict(String inputCitDistrict)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 11: All the cities in a district organised by largest population to smallest.
            String strQueryEight =
                    "SELECT city.Name as 'CityName', country.Name as 'CountryName', city.District as 'District', city.Population FROM country INNER JOIN city WHERE city.District='"+inputCitDistrict+"' AND country.Code=city.CountryCode ORDER BY city.Population DESC;";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryEight);
            // Extract city information
            ArrayList<City> district = new ArrayList<City>();
            while (rset.next())
            {
                City dist         = new City();
                dist.setCitName(rset.getString("CityName"));
                dist.setCountryName(rset.getString("CountryName"));
                dist.setCitDistrict(rset.getString("District"));
                dist.setCitPopulation(rset.getString("Population"));
                district.add(dist);
            }
            return district;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get all the cities in Queensland organised by largest population to smallest.");
            return null;
        }
    }

    /**
     * 11. All the cities in a district organised by largest population to smallest.
     * Formatting the output data from the list.
     **/
    public void printDistrict(ArrayList<City> district)
    {
        if (district == null){
            System.out.println("There is no city in Queensland");
        }
        //System.out.println("11. All the cities in a district organised by largest population to smallest.");
        // Print header
        System.out.println(String.format("%-35s %-40s %-25s %-25s","City Name","Country Name","District","Population"));
        // Loop over all cities in the list
        for (City di : district)
        {
            if (di == null)
                continue;
            String alldistString =
                    String.format("%-35s %-40s %-25s %-25s",
                            di.getCitName(),di.getCountryName(),di.getCitDistrict(),di.getCitPopulation());
            System.out.println(alldistString);
        }
    }
    /**
     * Outputs to Markdown
     * 11. All the cities in a district organised by largest population to smallest.
     * @param citDist
     */
    public void outputcityDistrict(ArrayList<City> citDist, String dist) {
        // Check cities is not null
        if (citDist == null) {
            System.out.println("Error !t");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int id = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | City Name | Country Name | District | Population |\r\n");
        sb.append("| --- | --- | --- | --- | --- |\r\n");
        // Loop over all districts in the list
        for (City citDt : citDist) {
            id += 1;
            if (citDt == null) continue;
            sb.append("| " + id + "| " + citDt.getCitName() + " | " + citDt.getCountryName() + " | " +
                    citDt.getCitDistrict() + " | " + numFormat.format(Integer.parseInt(citDt.getCitPopulation())) + " |\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + dist)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 12. The top N populated cities in the world where N is provided by the user.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<City> getTopNPopulatedCities(int inputTopWorld)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 12: The top N populated cities in the world where N is provided by the user.
            String strQueryEight =
                    "SELECT city.Name as 'Cityname', country.Name as 'Countryname', city.District, city.Population FROM city INNER JOIN country WHERE city.CountryCode=country.Code ORDER BY city.Population DESC LIMIT "+inputTopWorld+";";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryEight);
            // Extract cities information
            ArrayList<City> worlds = new ArrayList<City>();
            while (rset.next())
            {
                City world = new City();
                world.setCitName(rset.getString("Cityname"));
                world.setCountryName(rset.getString("Countryname"));
                world.setCitDistrict(rset.getString("District"));
                world.setCitPopulation(rset.getString("Population"));
                worlds.add(world);
            }
            return worlds;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the top 10 populated cities in the world.");
            return null;
        }
    }

    /** 12. The top N populated cities in the world where N is provided by the user.
     * Formatting the output data from the list.
     **/
    public void printTopNWorlds(ArrayList<City> wld)
    {
        if (wld == null){
            System.out.println("There is no top 10 populated cities in the world!");
        }
        //System.out.println("12. The top N populated cities in the world where N is provided by the user.");
        // Print header
        System.out.println(String.format("%-35s %-40s %-25s %-25s","City Name","Country Name","District","Population"));
        // Loop over all cities in the list
        for (City w : wld)
        {
            if (w == null)
                continue;
            String topwString =
                    String.format("%-35s %-40s %-25s %-25s",
                            w.getCitName(),w.getCountryName(),w.getCitDistrict(),w.getCitPopulation());
            System.out.println(topwString);
        }
    }
    /**
     * Outputs to Markdown
     * 12. The top N populated cities in the world where N is provided by the user.
     * @param topNWorld
     */
    public void outputTopNcityworld(ArrayList<City> topNWorld, String citWld) {
        // Check cities is not null
        if (topNWorld == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int id = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | City Name | Country Name | District | Population |\r\n");
        sb.append("| --- | --- | --- | --- | --- |\r\n");
        // Loop over top n city in the list
        for (City topWld : topNWorld) {
            id += 1;
            if (topWld == null) continue;
            sb.append("| " + id + "| " + topWld.getCitName() + " | " + topWld.getCountryName() + " | " +
                    topWld.getCitDistrict() + " | " + numFormat.format(Integer.parseInt(topWld.getCitPopulation())) + " |\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + citWld)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 13. The top N populated cities in a continent where N is provided by the user.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<City>getTopNPopulatedContinent(String inputTopContinent, int inputLimited)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 13: The top N populated cities in the continent where N is provided by the user.
            String strQueryEight =
                    "SELECT city.Name as 'CityName', country.Name as 'CountryName', city.District, country.Continent, city.Population FROM city INNER JOIN country on city.CountryCode = country.Code WHERE country.Continent = '"+inputTopContinent+"' ORDER BY city.Population DESC LIMIT "+ inputLimited +";";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryEight);
            // Extract city information
            ArrayList<City> continent = new ArrayList<City>();
            while (rset.next())
            {
                City conti            = new City();
                conti.setCitName(rset.getString("Cityname"));
                conti.setCountryName(rset.getString("Countryname"));
                conti.setCitDistrict(rset.getString("District"));
                conti.setCitCont(rset.getString("Continent"));
                conti.setCitPopulation(rset.getString("Population"));
                continent.add(conti);
            }
            return continent;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the top 10 populated cities in Europe.");
            return null;
        }
    }

    /**
     * 13. The top N populated cities in a continent where N is provided by the user.
     * Formatting the output data from the list.
     **/
    public void printTopNContinent(ArrayList<City> cnt)
    {
        if (cnt == null){
            System.out.println("There is no top 10 populated city in Europe.");
        }
        //System.out.println("13. The top N populated cities in a continent where N is provided by the user.");
        // Print header
        System.out.println(String.format("%-35s %-40s %-25s %-25s %-25s","City Name","Country Name","District","Continent","Population"));
        // Loop over all cities in the list
        for (City cont : cnt)
        {
            if (cont == null)
                continue;
            String topcontString =
                    String.format("%-35s %-40s %-25s %-25s %-25s",
                            cont.getCitName(),cont.getCountryName(),cont.getCitDistrict(),cont.getCitCont(),cont.getCitPopulation());
            System.out.println(topcontString);
        }
    }
    /**
     * Outputs to Markdown
     * 13. The top N populated cities in a continent where N is provided by the user.
     * @param topNCont
     */
    public void outputTopNcityCont(ArrayList<City> topNCont, String citCont) {
        // Check cities in a continent is not null
        if (topNCont == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int id = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | City Name | Country Name | District | Continent | Population |\r\n");
        sb.append("| --- | --- | --- | --- | --- | --- |\r\n");
        // Loop over top n continent in the list
        for (City topCont : topNCont) {
            id += 1;
            if (topCont == null) continue;
            sb.append("| " + id + "| " + topCont.getCitName() + " | " + topCont.getCountryName() + " | " +
                    topCont.getCitDistrict() + " | " + topCont.getCitCont() +" | " +
                    numFormat.format(Integer.parseInt(topCont.getCitPopulation())) + " |\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + citCont)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 14. The top N populated cities in a region where N is provided by the user.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<City> getTopNPopulatedRegion(String inputTopRegion, int inputLimited)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 14: The top N populated cities in a region where N is provided by the user.
            String strQueryEight =
                    "SELECT country.Region, city.Name as 'CityName', country.Name as 'CountryName', city.District, country.Region, city.Population FROM city INNER JOIN country on city.CountryCode = country.Code WHERE country.Region = '"+inputTopRegion+"' ORDER BY city.Population DESC LIMIT "+ inputLimited +";";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryEight);
            // Extract city information
            ArrayList<City> region = new ArrayList<City>();
            while (rset.next())
            {
                City reg          = new City();
                reg.setCitName(rset.getString("Cityname"));
                reg.setCountryName(rset.getString("Countryname"));
                reg.setCitDistrict(rset.getString("District"));
                reg.setCitReg(rset.getString("Region"));
                reg.setCitPopulation(rset.getString("Population"));
                region.add(reg);
            }
            return region;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the top 10 populated cities in Caribbean.");
            return null;
        }
    }

    /**
     * 14. The top N populated cities in a region where N is provided by the user.
     * Formatting the output data from the list.
     **/
    public void printTopNRegion(ArrayList<City> regn)
    {
        if (regn == null){
            System.out.println("There is no top 10 populated city in Caribbean");
        }
        //System.out.println("14. The top N populated cities in a region where N is provided by the user.");
        // Print header
        System.out.println(String.format("%-35s %-40s %-25s %-25s %-25s","City Name","Country Name","District","Region","Population"));
        // Loop over all cities in the list
        for (City cont : regn)
        {
            if (cont == null)
                continue;
            String topregString =
                    String.format("%-35s %-40s %-25s %-25s %-25s",
                            cont.getCitName(),cont.getCountryName(),cont.getCitDistrict(),cont.getCitReg(),cont.getCitPopulation());
            System.out.println(topregString);
        }
    }
    /**
     * Outputs to Markdown
     * 14. The top N populated cities in a region where N is provided by the user.
     * @param topNReg
     */
    public void outputTopNcityReg(ArrayList<City> topNReg, String citReg) {
        // Check cities in a region is not null
        if (topNReg == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int id = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | City Name | Country Name | District | Region | Population |\r\n");
        sb.append("| --- | --- | --- | --- | --- | --- |\r\n");
        // Loop over top n region in the list
        for (City topReg : topNReg) {
            id += 1;
            if (topReg == null) continue;
            sb.append("| " + id + "| " + topReg.getCitName() + " | " + topReg.getCountryName() + " | " +
                    topReg.getCitDistrict() + " | " + topReg.getCitReg() + " | " +
                    numFormat.format(Integer.parseInt(topReg.getCitPopulation())) + " |\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + citReg)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 15. The top N populated cities in a country where N is provided by the user.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<City> getTopNPopulatedCountries(String inputTopCountry, int inputLimited)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 15: The top N populated cities in a country where N is provided by the user.
            String strQueryEight =
                    "SELECT city.Name as 'CityName',country.Name as 'CountryName',city.District, city.Population FROM city INNER JOIN country on city.CountryCode = country.Code WHERE country.Name = '"+inputTopCountry+"' ORDER BY city.Population DESC LIMIT "+ inputLimited +";";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryEight);
            // Extract city information
            ArrayList<City> country = new ArrayList<City>();
            while (rset.next())
            {
                City cty = new City();
                cty.setCitName(rset.getString("CityName"));
                cty.setCountryName(rset.getString("CountryName"));
                cty.setCitDistrict(rset.getString("District"));
                cty.setCitPopulation(rset.getString("Population"));
                country.add(cty);
            }
            return country;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the top 10 populated cities in Argentina.");
            return null;
        }
    }
    /**
     * 15. The top N populated cities in a country where N is provided by the user.
     * Formatting the output data from the list.
     **/
    public void printTopNCountries(ArrayList<City> count)
    {
        if (count == null){
            System.out.println("There is no top 10 populated city in Argentina.");
        }
        //System.out.println("15. The top N populated cities in a country where N is provided by the user.");
        // Print header
        System.out.println(String.format("%-35s %-40s %-25s %-30s","Country","City Name","District","Population"));
        // Loop over all cities in the list
        for (City cont : count)
        {
            if (cont == null)
                continue;
            String topcouString =
                    String.format("%-35s %-40s %-25s %-30s",
                            cont.getCitName(),cont.getCountryName(),cont.getCitDistrict(),cont.getCitPopulation());
            System.out.println(topcouString);
        }
    }
    /**
     * Outputs to Markdown
     * 15. The top N populated cities in a country where N is provided by the user.
     * @param topNcty
     */
    public void outputTopNcitycty(ArrayList<City> topNcty, String citcty) {
        // Check cities in a country is not null
        if (topNcty == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int id = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | City Name | Country Name | District | Population |\r\n");
        sb.append("| --- | --- | --- | --- | --- |\r\n");
        // Loop over top n country in the list
        for (City topcty : topNcty) {
            id += 1;
            if (topcty == null) continue;
            sb.append("| " + id + "| " + topcty.getCitName() + " | " + topcty.getCountryName() + " | " +
                    topcty.getCitDistrict() + " | " + numFormat.format(Integer.parseInt(topcty.getCitPopulation())) + " |\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + citcty)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 16. The top N populated cities in a district where N is provided by the user.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<City> getTopNPopulatedDistrict(String inputTopDistrict, int inputLimited)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 13: The top N populated cities in a district  where N is provided by the user.
            String strQueryEight =
                    "SELECT city.Name as 'CityName', country.name as 'CountryName', city.District, city.Population FROM city INNER JOIN country on city.CountryCode = country.Code WHERE city.District = '"+inputTopDistrict+"' ORDER BY city.Population DESC LIMIT "+ inputLimited +";";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryEight);
            // Extract city information
            ArrayList<City> dist = new ArrayList<City>();
            while (rset.next())
            {
                City dis = new City();
                dis.setCitName(rset.getString("Cityname"));
                dis.setCountryName(rset.getString("Countryname"));
                dis.setCitDistrict(rset.getString("District"));
                dis.setCitPopulation(rset.getString("Population"));
                dist.add(dis);
            }
            return dist;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the top 10 populated cities in Zuid-Holland.");
            return null;
        }
    }

    /**
     * 16. The top N populated cities in a district where N is provided by the user.
     * Formatting the output data from the list.
     **/
    public void printTopNDistrict(ArrayList<City> dists)
    {
        if (dists == null){
            System.out.println("There is no top 10 populated city in Zuid-Holland");
        }
        //System.out.println("16. The top N populated cities in a district where N is provided by the user.");
        // Print header
        System.out.println(String.format("%-30s %-40s %-25s %-30s","City Name","Country Name","District","Population"));
        // Loop over all cities in the list
        for (City cont : dists)
        {
            if (cont == null)
                continue;
            String topdistString =
                    String.format("%-30s %-40s %-25s %-30s",
                            cont.getCitName(),cont.getCountryName(),cont.getCitDistrict(),cont.getCitPopulation());
            System.out.println(topdistString);
        }

    }
    /**
     * Outputs to Markdown
     * 16. The top N populated cities in a district where N is provided by the user.
     * @param topNdist
     */
    public void outputTopNcitydist(ArrayList<City> topNdist, String citdist) {
        // Check cities in a district is not null
        if (topNdist == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int id = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | City Name | Country Name | District | Population |\r\n");
        sb.append("| --- | --- | --- | --- | --- |\r\n");
        // Loop over top n district in the list
        for (City topdist : topNdist) {
            id += 1;
            if (topdist == null) continue;
            sb.append("| " + id + "| " + topdist.getCitName() + " | " + topdist.getCountryName() + " | " +
                    topdist.getCitDistrict() + " | " + numFormat.format(Integer.parseInt(topdist.getCitPopulation())) + " |\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + citdist)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 17.All the capital cities in the world organised by largest population to smallest.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<CapitalCity> getAllCapitalCities()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 17.All the capital cities in the world organised by largest population to smallest.
            String strQuerySeventeen =
                    "SELECT city.Name as 'CapitalCity', country.Name as 'CountryName', country.Population FROM city INNER JOIN country WHERE city.ID = country.Capital AND country.Code=city.CountryCode ORDER BY country.Population DESC;";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQuerySeventeen);
            // Extract capital cities information
            ArrayList<CapitalCity> allCapitalCity = new ArrayList<CapitalCity>();
            while (rset.next())
            {
                CapitalCity capcit = new CapitalCity();
                capcit.setCapCityName(rset.getString("CapitalCity"));
                capcit.setCapCityCountry(rset.getString("CountryName"));
                capcit.setCapCityPopulation(rset.getString("Population"));
                allCapitalCity.add(capcit);
            }
            return allCapitalCity;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get all the capital cities in the world organised by largest population to smallest.");
            return null;
        }
    }
    /**
     * 17.All the capital cities in the world organised by largest population to smallest.
     * Formatting the output data from the list.
     **/
    public void printAllCapitalCities(ArrayList<CapitalCity> capitalCities)
    {
        // Check capital city is not null
        if (capitalCities == null)
        {
            System.out.println("There is no capital city in the world.");
            return;
        }
        // Print header
        System.out.println(String.format("%-40s %-40s %-25s", "CapitalCity","Country","Population"));
        // Loop over all capital city in the list
        for (CapitalCity cc : capitalCities)
        {
            if (cc == null)
                continue;

            String capctrString =
                    String.format("%-40s %-40s %-25s",
                            cc.getCapCityName(),cc.getCapCityCountry(),cc.getCapCityPopulation());
            System.out.println(capctrString);
        }
    }
    /**
     * Outputs to Markdown
     * 17.All the capital cities in the world organised by largest population to smallest.
     * @param allcapitalcity
     */
    public void outputCapitalCity(ArrayList<CapitalCity> allcapitalcity, String AllCapitalCitiesTable) {
        // Check capital city is not null
        if (allcapitalcity == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int id = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | Capital City Name | Country | Population |\r\n");
        sb.append("| --- | --- | --- | --- |\r\n");
        // Loop over all capital cities in the list
        for (CapitalCity capcit : allcapitalcity) {
            id += 1;
            if (capcit == null) continue;
            sb.append("| " + id + "| " + capcit.getCapCityName() + " | " +
                    capcit.getCapCityCountry() + " | " + numFormat.format(Integer.parseInt(capcit.getCapCityPopulation())) + "|\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + AllCapitalCitiesTable)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 18.All the capital cities in a continent organised by largest population to smallest.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<CapitalCity> getAllCapitalCitiesContinents(String inputCont)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 18.All the capital cities in a continent organised by largest population to smallest.
            String strQueryEighteen =
                    "SELECT city.Name as 'CapitalCity', country.Name as 'CountryName', country.Continent, country.Population FROM city INNER JOIN country WHERE city.ID = country.Capital AND country.Code=city.CountryCode AND country.Continent = '"+ inputCont +"' ORDER BY country.Population DESC;";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryEighteen);
            // Extract capital city information
            ArrayList<CapitalCity> capCitContinent = new ArrayList<CapitalCity>();
            while (rset.next())
            {
                CapitalCity capcitCont = new CapitalCity();
                capcitCont.setCapCityName(rset.getString("CapitalCity"));
                capcitCont.setCapCityCountry(rset.getString("CountryName"));
                capcitCont.setCapCityContinent(rset.getString("Continent"));
                capcitCont.setCapCityPopulation(rset.getString("Population"));
                capCitContinent.add(capcitCont);
            }
            return capCitContinent;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get all the capital city in Asia organised by largest population to smallest.");
            return null;
        }
    }

    /**
     * 18.All the capital cities in a continent organised by largest population to smallest.
     * Formatting the output data from the list.
     **/
    public void printAllCapitalCityContinent(ArrayList<CapitalCity> capcitContinent)
    {
        // Check continent is not null
        if (capcitContinent == null)
        {
            System.out.println("There is no capital city in Asia.");
            return;
        }
        // Print header
        System.out.println(String.format("%-30s %-40s %-30s %-25s", "CapitalCity", "Country", "Continent", "Population"));
        // Loop over all continent in the list
        for (CapitalCity cccon : capcitContinent)
        {
            if (cccon == null)
                continue;
            String capconString =
                    String.format("%-30s %-40s %-30s %-25s",
                            cccon.getCapCityName(), cccon.getCapCityCountry(), cccon.getCapCityContinent(), cccon.getCapCityPopulation());
            System.out.println(capconString);
        }
    }

    /**
     * Outputs to Markdown
     * 18.All the capital cities in a continent organised by largest population to smallest.
     * @param allcapitalcitycont
     */
    public void outputCapitalCityCont(ArrayList<CapitalCity> allcapitalcitycont, String AllCapitalCitiesContinentTable) {
        // Check continent is not null
        if (allcapitalcitycont == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int id = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | Capital City Name | Country | Continent | Population |\r\n");
        sb.append("| --- | --- | --- | --- | --- |\r\n");
        // Loop over all continents in the list
        for (CapitalCity capcitcon : allcapitalcitycont) {
            id += 1;
            if (capcitcon == null) continue;
            sb.append("| " + id + "| " + capcitcon.getCapCityName() + " | " + capcitcon.getCapCityCountry() + " | " +
                    capcitcon.getCapCityContinent()+ " | " + numFormat.format(Integer.parseInt(capcitcon.getCapCityPopulation())) + "|\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + AllCapitalCitiesContinentTable)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 19.All the capital cities in a region organised by largest to smallest.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<CapitalCity> getAllCapitalCitiesRegions(String inputRegi)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 19.All the capital cities in a region organised by largest to smallest.
            String strQueryNineteen =
                    "SELECT city.Name as 'CapitalCity', country.Name as 'CountryName', country.Region, country.Population FROM city INNER JOIN country WHERE city.ID = country.Capital AND country.Code=city.CountryCode AND country.Region = '"+ inputRegi +"' ORDER BY country.Population DESC;";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryNineteen);
            // Extract capital city information
            ArrayList<CapitalCity> capCitRegion = new ArrayList<CapitalCity>();
            while (rset.next())
            {
                CapitalCity capcitreg          = new CapitalCity();
                capcitreg.setCapCityName(rset.getString("CapitalCity"));
                capcitreg.setCapCityCountry(rset.getString("CountryName"));
                capcitreg.setCapCityRegion(rset.getString("Region"));
                capcitreg.setCapCityPopulation(rset.getString("Population"));
                capCitRegion.add(capcitreg);
            }
            return capCitRegion;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get all the capital cities in Caribbean organised by largest population to smallest.");
            return null;
        }
    }
    /**
     * 19. All the capital cities in a region organised by largest to smallest.
     * Formatting the output data from the list.
     **/
    public void printAllCapitalCityRegion(ArrayList<CapitalCity> capcitRegion)
    {
        // Check region is not null
        if (capcitRegion == null)
        {
            System.out.println("There is no capital city in Caribbean.");
            return;
        }
        // Print header
        System.out.println(String.format("%-30s %-40s %-30s %-25s", "CapitalCity","Country","Region", "Population"));
        // Loop over all region in the list
        for (CapitalCity ccr : capcitRegion)
        {
            //printRegion to check if an region is null
            if (ccr == null)
                continue;
            String regString =
                    String.format("%-30s %-40s %-30s %-25s",
                            ccr.getCapCityName(),ccr.getCapCityCountry(),ccr.getCapCityRegion(), ccr.getCapCityPopulation());
            System.out.println(regString);
        }
    }
    /**
     * Outputs to Markdown
     * 19. All the capital cities in a region organised by largest to smallest.
     * @param allcapitalcityreg
     */
    public void outputCapitalCityReg(ArrayList<CapitalCity> allcapitalcityreg, String AllCapitalCitiesRegionTable) {
        // Check region is not null
        if (allcapitalcityreg == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int id = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | Capital City Name | Country | Region | Population |\r\n");
        sb.append("| --- | --- | --- | --- | --- |\r\n");
        // Loop over all regions in the list
        for (CapitalCity capcitreg : allcapitalcityreg) {
            id += 1;
            if (capcitreg == null) continue;
            sb.append("| " + id + "| " + capcitreg.getCapCityName() + " | " + capcitreg.getCapCityCountry() + " | " +
                    capcitreg.getCapCityRegion()+ " | " + numFormat.format(Integer.parseInt(capcitreg.getCapCityPopulation())) + "|\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + AllCapitalCitiesRegionTable)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 20.The top N populated capital cities in the world where N is provided by the user.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<CapitalCity> getTopNCapCitiesWorld(int inputLimit)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 20.The top N populated capital cities in the world where N is provided by the user.
            String strQueryTwenty =
                    "SELECT city.Name as 'CityName', country.Name as 'CountryName', country.Population FROM city INNER JOIN country WHERE city.ID = country.Capital AND country.Code=city.CountryCode ORDER BY country.Population DESC LIMIT "+ inputLimit +";";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryTwenty);
            // Extract capital city information
            ArrayList<CapitalCity> capWorld = new ArrayList<CapitalCity>();
            while (rset.next())
            {
                CapitalCity capWld = new CapitalCity();
                capWld.setCapCityName(rset.getString("CityName"));
                capWld.setCapCityCountry(rset.getString("CountryName"));
                capWld.setCapCityPopulation(rset.getString("Population"));
                capWorld.add(capWld);
            }
            return capWorld;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the top 10 populated capital cities in the world.");
            return null;
        }
    }
    /**
     * 20. The top N populated capital cities in the world where N is provided by the user.
     * Formatting the output data from the list.
     **/
    public void printTopNCapCitiesWorld(ArrayList<CapitalCity> capWorld)
    {
        // Check capital city is not null
        if (capWorld == null)
        {
            System.out.println("There is no top 10 populated capital city in the world.");
            return;
        }
        // Print header
        System.out.println(String.format("%-30s %-40s %-30s", "Capital City Name","Country Name", "Population"));
        // Loop over top n capital city in the world
        for (CapitalCity ccr : capWorld)
        {
            //print the list to check if capital cities in the world is null
            if (ccr == null)
                continue;
            String wString =
                    String.format("%-30s %-40s %-30s",
                            ccr.getCapCityName(),ccr.getCapCityCountry(), ccr.getCapCityPopulation());
            System.out.println(wString);
        }
    }
    /**
     * Outputs to Markdown
     * 20. The top 10 populated capital cities in the world where N is provided by the user.
     * @param top10capitalcity
     */
    public void outputTop10CapitalCity(ArrayList<CapitalCity> top10capitalcity, String Top10CapitalCityTable) {
        // Check capital city is not null
        if (top10capitalcity == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int id = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | Capital City Name | Country | Population |\r\n");
        sb.append("| --- | --- | --- | --- |\r\n");
        // Loop over top n capital city in the list
        for (CapitalCity topcapcit : top10capitalcity) {
            id += 1;
            if (topcapcit == null) continue;
            sb.append("| " + id + "| " + topcapcit.getCapCityName() + " | " + topcapcit.getCapCityCountry() + " | " +
                    numFormat.format(Integer.parseInt(topcapcit.getCapCityPopulation())) + "|\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + Top10CapitalCityTable)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 21.The top N populated capital cities in a continent where N is provided by the user.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<CapitalCity> getTopNCapCitiesCont(String inputContin, int inputLimit)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 20.The top N populated capital cities in a continent where N is provided by the user.
            String strQueryTwentyOne =
                    "SELECT city.Name as 'CityName', country.Name as 'CountryName',country.Continent, country.Population FROM city INNER JOIN country WHERE city.ID = country.Capital AND country.Code=city.CountryCode AND country.Continent = '"+ inputContin +"' ORDER BY country.Population DESC LIMIT "+ inputLimit +";";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryTwentyOne);
            // Extract capital city information
            ArrayList<CapitalCity> capCont = new ArrayList<CapitalCity>();
            while (rset.next())
            {
                CapitalCity capCnt = new CapitalCity();
                capCnt.setCapCityName(rset.getString("CityName"));
                capCnt.setCapCityCountry(rset.getString("CountryName"));
                capCnt.setCapCityContinent(rset.getString("Continent"));
                capCnt.setCapCityPopulation(rset.getString("Population"));
                capCont.add(capCnt);
            }
            return capCont;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the top 10 populated capital cities in North America.");
            return null;
        }
    }
    /**
     * 21. The top N populated capital cities in a continent where N is provided by the user.
     * Formatting the output data from the list.
     **/
    public void printTopNCapCitiesCont(ArrayList<CapitalCity> ncontCap)
    {
        // Check continent is not null
        if (ncontCap == null)
        {
            System.out.println("There is no capital city in North America.");
            return;
        }
        // Print header
        System.out.println(String.format("%-30s %-40s %-35s %-30s", "Capital City Name","Country Name","Continent", "Population"));
        // Loop over all capital cities in a continent
        for (CapitalCity ccr : ncontCap)
        {
            //print the list to check if capital cities in a continent is null
            if (ccr == null)
                continue;
            String coString =
                    String.format("%-30s %-40s %-35s %-30s",
                            ccr.getCapCityName(),ccr.getCapCityCountry(), ccr.getCapCityContinent(),ccr.getCapCityPopulation());
            System.out.println(coString);
        }
    }
    /**
     * Outputs to Markdown
     * 21. The top 10 populated capital cities in a continent where N is provided by the user.
     * @param top10capitalcitycont
     */
    public void outputTop10CapitalCityCont(ArrayList<CapitalCity> top10capitalcitycont, String Top10CapitalCityContTable) {
        // Check continent is not null
        if (top10capitalcitycont == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int id = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | Capital City Name | Country | Continent | Population |\r\n");
        sb.append("| --- | --- | --- | --- | --- |\r\n");
        // Loop over top n continent in the list
        for (CapitalCity topcapcitcon : top10capitalcitycont) {
            id += 1;
            if (topcapcitcon == null) continue;
            sb.append("| " + id +  "| " + topcapcitcon.getCapCityName() + " | " + topcapcitcon.getCapCityCountry() + " | " +
                    topcapcitcon.getCapCityContinent() + " | " + numFormat.format(Integer.parseInt(topcapcitcon.getCapCityPopulation())) + "|\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + Top10CapitalCityContTable)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 22.The top N populated capital cities in a region where N is provided by the user.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<CapitalCity> getTopNCapCitiesReg(String inputTopRegion, int inputLimit)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 20.The top N populated capital cities in a region where N is provided by the user.
            String strQueryTwentyTwo =
                    "SELECT city.Name as 'CityName', country.Name as 'CountryName',country.Region, country.Population FROM city INNER JOIN country WHERE city.ID = country.Capital AND country.Code=city.CountryCode AND country.Region='"+ inputTopRegion +"' ORDER BY country.Population DESC LIMIT "+ inputLimit +";";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryTwentyTwo);
            // Extract capital city information
            ArrayList<CapitalCity> capRegion = new ArrayList<CapitalCity>();
            while (rset.next())
            {
                CapitalCity capRegi = new CapitalCity();
                capRegi.setCapCityName(rset.getString("CityName"));
                capRegi.setCapCityCountry(rset.getString("CountryName"));
                capRegi.setCapCityRegion(rset.getString("Region"));
                capRegi.setCapCityPopulation(rset.getString("Population"));
                capRegion.add(capRegi);
            }
            return capRegion;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the top 10 populated capital cities in Middle East.");
            return null;
        }
    }
    /**
     * 22. The top N populated capital cities in a continent where N is provided by the user.
     * Formatting the output data from the list.
     **/
    public void printTopNCapCitiesReg(ArrayList<CapitalCity> regCap)
    {
        // Check region is not null
        if (regCap == null)
        {
            System.out.println("There is no capital city in Middle East.");
            return;
        }
        // Print header
        System.out.println(String.format("%-30s %-40s %-35s %-30s", "Capital City Name","Country Name", "Region","Population"));
        // Loop over all capital cities in a region
        for (CapitalCity ccr : regCap)
        {
            //print the list to check if capital cities in a region is null
            if (ccr == null)
                continue;
            String reString =
                    String.format("%-30s %-40s %-35s %-30s",
                            ccr.getCapCityName(),ccr.getCapCityCountry(), ccr.getCapCityRegion(), ccr.getCapCityPopulation());
            System.out.println(reString);
        }
    }

    /**
     * Outputs to Markdown
     * 22. The top 10 populated capital cities in a region where N is provided by the user.
     * @param top10capitalcityreg
     */
    public void outputTop10CapitalCityReg(ArrayList<CapitalCity> top10capitalcityreg, String Top10CapitalCityRegTable) {
        // Check region is not null
        if (top10capitalcityreg == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int id = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | Capital City Name | Country | Region | Population |\r\n");
        sb.append("| --- | --- | --- | --- | --- |\r\n");
        // Loop over top n region in the list
        for (CapitalCity topcapcitreg : top10capitalcityreg) {
            id += 1;
            if (topcapcitreg == null) continue;
            sb.append("| " + id + "| " + topcapcitreg.getCapCityName() + " | " + topcapcitreg.getCapCityCountry() + " | " +
                    topcapcitreg.getCapCityRegion() + " | " + numFormat.format(Integer.parseInt(topcapcitreg.getCapCityPopulation())) + "|\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + Top10CapitalCityRegTable)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 23.The population of people, people living in cities, and people not living in cities in each continent.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<PeoplePopulation> getPopulatedPeopleContinent()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 23.The population of people, people living in cities, and people not living in cities in each continent.
            String strQueryTwentyThree =
                    "SELECT country.Continent, SUM(country.Population) as 'ContinentPopulation', SUM((SELECT SUM(city.Population) FROM city WHERE city.CountryCode=country.Code)) as 'CityPopulation' FROM country GROUP BY country.Continent;";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryTwentyThree);
            // Extract people population information
            ArrayList<PeoplePopulation> populationCont = new ArrayList<PeoplePopulation>();
            while (rset.next())
            {
                PeoplePopulation popCont = new PeoplePopulation();
                popCont.setContinentName(rset.getString("Continent"));
                popCont.setContinentPopulation(rset.getString("ContinentPopulation"));
                popCont.setCityPopulation(rset.getString("CityPopulation"));
                populationCont.add(popCont);
            }
            return populationCont;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the population of people, people living in cities, and people not living in cities in each continent.");
            return null;
        }
    }
    /**
     * 23. The population of people, people living in cities, and people not living in cities in each continent.
     * Formatting the output data from the list.
     **/
    public void printPopulatedPeopleConitnent(ArrayList<PeoplePopulation> popuConti)
    {
        // Check continent is not null
        if (popuConti == null)
        {
            System.out.println("There is no population of people, people living in cities, and people not living in cities in each continent.");
            return;
        }
        // Print header
        System.out.println(String.format("%-20s %-28s %-25s %-25s %-25s", "Continent","Continent Total Population","City Total Population","People Not Living (%)","People Living (%)"));
        // Loop over people population in a continent

        for (PeoplePopulation pcon : popuConti)
        {
            //print the list to check if people population in a continent is null
            if (pcon == null)
                continue;
            if (pcon.getCityPopulation() == null){
                pcon.setCityPopulation("0");
            }
            float conpopulation = Float.parseFloat(pcon.getContinentPopulation());
            float concitypopulation = Float.parseFloat(pcon.getCityPopulation());
            float livingconper = 100 * (concitypopulation/conpopulation);
            float notlivingcon = 100 - livingconper;
            String nan = "NaN";
            if(String.valueOf(livingconper) == nan){
                livingconper = 0.0F;
            }
            if(String.valueOf(notlivingcon) == nan){
                notlivingcon = 0.0F;
            }

            if(livingconper == 0.0F && notlivingcon == 0.0F)
            {
                String strnotlivingconper = notlivingcon+"%";
                String strlivingconper = livingconper+"%";

                pcon.setLivingPopContPer(strlivingconper);
                pcon.setNotLivingPopContPer(strnotlivingconper);
            }
            else
            {
                String pattern="###.00";
                DecimalFormat df=new DecimalFormat(pattern);

                String formatnotlivingconper = df.format(notlivingcon);
                String formatlivingconper = df.format(livingconper);

                String strnotlivingconper = formatnotlivingconper+"%";
                String strlivingconper = formatlivingconper+"%";

                pcon.setLivingPopContPer(strlivingconper);
                pcon.setNotLivingPopContPer(strnotlivingconper);
            }

            String pconString =
                    String.format("%-20s %-28s %-25s %-25s %-25s",
                            pcon.getContinentName(),pcon.getContinentPopulation(), pcon.getCityPopulation(), pcon.getNotLivingPopContPer(), pcon.getLivingPopContPer());
            System.out.println(pconString);
        }
    }

    /**
     * Outputs to Markdown
     * 23. The population of people, people living in cities, and people not living in cities in each continent.
     * @param populationContinent
     */
    public void outputPopulationContinent(ArrayList<PeoplePopulation> populationContinent, String populationContinentReport) {
        // Check people population is not null
        if (populationContinent == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int id = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | Continent Name | Continent Total Population | City Total Population | People Not Living (%) | People Living (%) |\r\n");
        sb.append("| --- | --- | --- | --- | --- | --- |\r\n");

        // Loop over continent people population in the list
        for (PeoplePopulation popcontinent : populationContinent) {
            id += 1;
            if (popcontinent == null) continue;

            sb.append("| " + id + "| " + popcontinent.getContinentName() + " | " +
                    numFormat.format(Long.parseLong(popcontinent.getContinentPopulation())) + " | " +
                            numFormat.format(Long.parseLong(popcontinent.getCityPopulation())) + " | " +
                    popcontinent.getNotLivingPopContPer() + " | " + popcontinent.getLivingPopContPer() + " | "
                    + "|\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + populationContinentReport)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 24.The population of people, people living in cities, and people not living in cities in each region.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<PeoplePopulation> getPopulatedPeopleRegions()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 24.The population of people, people living in cities, and people not living in cities in each region.
            String strQueryTwentyFour =
                    "SELECT country.Region, SUM(country.Population) as 'RegionPopulation', SUM((SELECT SUM(city.Population) FROM city WHERE city.CountryCode=country.Code)) as 'CityPopulation' FROM country GROUP BY country.Region;";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryTwentyFour);
            // Extract people population information
            ArrayList<PeoplePopulation> populationReg = new ArrayList<PeoplePopulation>();
            while (rset.next())
            {
                PeoplePopulation popReg = new PeoplePopulation();
                popReg.setRegionsName(rset.getString("Region"));
                popReg.setRegionsPopulation(rset.getString("RegionPopulation"));
                popReg.setCityPopulation(rset.getString("CityPopulation"));
                populationReg.add(popReg);
            }
            return populationReg;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the population of people, people living in cities, and people not living in cities in each region.");
            return null;
        }
    }
    /**
     * 24. The population of people, people living in cities, and people not living in cities in each region.
     * Formatting the output data from the list.
     **/
    public void printPopulatedPeopleRegions(ArrayList<PeoplePopulation> popuRegs)
    {
        // Check region is not null
        if (popuRegs == null)
        {
            System.out.println("There is no population of people, people living in cities, and people not living in cities in each region.");
            return;
        }
        // Print header
        System.out.println(String.format("%-30s %-25s %-25s %-25s %-25s", "Region", "Region Total Population","City Total Population", "People Not Living (%)", "People Living (%)"));
        // Loop over people population in a region
        for (PeoplePopulation preg : popuRegs)
        {
            //print the list to check if people population in a region is null
            if (preg == null)
                continue;
            if (preg.getCityPopulation() == null){
                preg.setCityPopulation("0");
            }
            float regpopulation = Float.parseFloat(preg.getRegionsPopulation());
            float regcitypopulation = Float.parseFloat(preg.getCityPopulation());
            float livingregper = 100 * (regcitypopulation/regpopulation);
            float notlivingregper = 100 - livingregper;
            String nan = "NaN";
            if(String.valueOf(livingregper) == nan){
                livingregper = 0.0F;
            }
            if(String.valueOf(notlivingregper) == nan){
                notlivingregper = 0.0F;
            }

            if(livingregper == 0.0F && notlivingregper == 0.0F)
            {
                String strnotlivingregper = livingregper+"%";
                String strlivingregper = notlivingregper+"%";

                preg.setLivingPopRegPer(strlivingregper);
                preg.setNotLivingPopRegPer(strnotlivingregper);
            }
            else
            {
                String pattern="###.00";
                DecimalFormat df=new DecimalFormat(pattern);

                String formatnotlivingregper = df.format(notlivingregper);
                String formatlivingregper = df.format(livingregper);

                String strnotlivingregper = formatnotlivingregper+"%";
                String strlivingregper = formatlivingregper+"%";

                preg.setLivingPopRegPer(strlivingregper);
                preg.setNotLivingPopRegPer(strnotlivingregper);
            }


            String pregString =
                    String.format("%-30s %-25s %-25s %-25s %-25s",
                            preg.getRegionsName(),preg.getRegionsPopulation(),preg.getCityPopulation(),preg.getNotLivingPopRegPer(),preg.getLivingPopRegPer());
            System.out.println(pregString);
        }
    }

    /**
     * Outputs to Markdown
     * 24. The population of people, people living in cities, and people not living in cities in each region.
     * @param populationRegion
     */
    public void outputPopulationRegion(ArrayList<PeoplePopulation> populationRegion, String populationRegionReport) {
        // Check people population is not null
        if (populationRegion == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int id = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | Region Name | Total Region Population | Total City Population | People Not Living (%) | People Living (%) |\r\n");
        sb.append("| --- | --- | --- | --- | --- | --- |\r\n");
        // Loop over region people population in the list
        for (PeoplePopulation poprgn : populationRegion) {
            id += 1;
            if (poprgn == null) continue;
            sb.append("| " + id + "| " + poprgn.getRegionsName() + " | " +
                    numFormat.format(Long.parseLong(poprgn.getRegionsPopulation())) + " | " +
                            numFormat.format(Long.parseLong(poprgn.getCityPopulation())) + " | " +
                    poprgn.getNotLivingPopRegPer() + " | " + poprgn.getLivingPopRegPer() + " | "
                    + "|\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + populationRegionReport)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 25.The population of people, people living in cities, and people not living in cities in each country.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<PeoplePopulation> getPopulatedPeopleCountry()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 25.The population of people, people living in cities, and people not living in cities in each country.
            String strQueryTwentyFive =
                    "SELECT country.Name, SUM(country.Population) as 'CountryPopulation', SUM((SELECT SUM(city.Population) FROM city WHERE city.CountryCode=country.Code)) as 'CityPopulation' FROM country GROUP BY country.Name;";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryTwentyFive);
            // Extract people population information
            ArrayList<PeoplePopulation> populationCou = new ArrayList<PeoplePopulation>();
            while (rset.next())
            {
                PeoplePopulation popCou = new PeoplePopulation();
                popCou.setCountriesName(rset.getString("Name"));
                popCou.setCountriesPopulation(rset.getString("CountryPopulation"));
                popCou.setCityPopulation(rset.getString("CityPopulation"));
                populationCou.add(popCou);
            }
            return populationCou;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the population of people, people living in cities, and people not living in cities in each country.");
            return null;
        }
    }
    /**
     * 25. The population of people, people living in cities, and people not living in cities in each country.
     * Formatting the output data from the list.
     **/
    public void printPopulatedPeopleCountry(ArrayList<PeoplePopulation> popuCoun)
    {
        // Check country is not null
        if (popuCoun == null)
        {
            System.out.println("There is no population of people, people living in cities, and people not living in cities in each country.");
            return;
        }
        // Print header
        System.out.println(String.format("%-45s %-28s %-25s %-25s %-25s", "Country Name" ,"Country Total Population" ,"City Total Population", "People Not Living(%)", "People Living(%)"));
        // Loop over people population in a country
        for (PeoplePopulation pcou : popuCoun)
        {
            //print the list to check if people population in a country is null
            if (pcou == null)
                continue;
            if (pcou.getCityPopulation() == null){
                pcou.setCityPopulation("0");
            }
            float coupopulation = Float.parseFloat(pcou.getCountriesPopulation());
            float coucitypopulation = Float.parseFloat(pcou.getCityPopulation());
            float livingcouper = 100 * (coucitypopulation/coupopulation);
            float notlivingcouper = 100 - livingcouper;
            String nan = "NaN";
            if(String.valueOf(livingcouper) == nan){
                livingcouper = 0.0F;
            }
            if(String.valueOf(notlivingcouper) == nan){
                notlivingcouper = 0.0F;
            }

            if(livingcouper == 0.0F && notlivingcouper == 0.0F)
            {
                String strlivingcouper = livingcouper+"%";
                String strnotlivingcouper = notlivingcouper+"%";

                pcou.setLivingPopCountryPer(strlivingcouper);
                pcou.setNotLivingPopCountryPer(strnotlivingcouper);
            }
            else
            {
                String pattern="###.00";
                DecimalFormat df=new DecimalFormat(pattern);

                String formatnotlivingcouper = df.format(notlivingcouper);
                String formatlivingcouper = df.format(livingcouper);

                String strnotlivingcouper = formatnotlivingcouper+"%";
                String strlivingcouper = formatlivingcouper+"%";

                pcou.setLivingPopCountryPer(strlivingcouper);
                pcou.setNotLivingPopCountryPer(strnotlivingcouper);
            }

            String pcouString =
                    String.format("%-45s %-28s %-25s %-25s %-25s",
                            pcou.getCountriesName(), pcou.getCountriesPopulation(), pcou.getCityPopulation(), pcou.getNotLivingPopCountryPer(), pcou.getLivingPopCountryPer());
            System.out.println(pcouString);
        }
    }
    /**
     * Outputs to Markdown
     * 25. The population of people, people living in cities, and people not living in cities in each country.
     * @param populationCountry
     */
    public void outputPopulationCountry(ArrayList<PeoplePopulation> populationCountry, String populationCountryReport) {
        // Check people population is not null
        if (populationCountry == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int id = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | Country Name | Total Country Population | Total City Population | People Not Living (%) | People Living (%) |\r\n");
        sb.append("| --- | --- | --- | --- | --- | --- |\r\n");
        // Loop over country people population in the list
        for (PeoplePopulation popcoun : populationCountry) {
            id += 1;
            if (popcoun == null) continue;
            sb.append("| " + id + "| " + popcoun.getCountriesName() + " | " +
                    numFormat.format(Long.parseLong(popcoun.getCountriesPopulation())) + " | " +
                            numFormat.format(Long.parseLong(popcoun.getCityPopulation())) + " | " +
                                    popcoun.getNotLivingPopCountryPer() + " | " + popcoun.getLivingPopCountryPer() + " | "
                    + "|\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + populationCountryReport)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 26.The population of the world.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<PeoplePopulation> getWorldPopulation()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 26.The population of the world.
            String strQueryTwentySix =
                    "SELECT SUM(country.Population) as 'Population' FROM country;";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryTwentySix);
            // Extract people population information
            ArrayList<PeoplePopulation> populationWorld = new ArrayList<PeoplePopulation>();
            while (rset.next())
            {
                PeoplePopulation popWorld = new PeoplePopulation();
                popWorld.setWorldPopulation(rset.getString("Population"));
                populationWorld.add(popWorld);
            }
            return populationWorld;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the people population of the world.");
            return null;
        }
    }
    /**
     * 26. The population of the world.
     * Formatting the output data from the list.
     **/
    public void printWorldPopulation(ArrayList<PeoplePopulation> popWorld)
    {

        // Check country is not null
        if (popWorld == null) {
            System.out.println("There is no people population of the world.");
            return;
        }
        // Loop over people population in the world
        for (PeoplePopulation wpop : popWorld) {
            //print the list to check if people population in the world is null
            if (wpop == null)
                continue;
            String pworldString =
                    String.format("%-40s",
                            wpop.getWorldPopulation());
            System.out.println("Total World People Population: "+pworldString);
        }
    }

    /**
     * Outputs to Markdown
     * 26. The population of the world.
     * @param populationOfWorld
     */
    public void outputPopulationOfWorld(ArrayList<PeoplePopulation> populationOfWorld, String populationOfWorldReport) {
        // Check people population is not null
        if (populationOfWorld == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int idnum = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | Total World Population |\r\n");
        sb.append("| --- | --- |\r\n");
        // Loop over people population in the list
        for (PeoplePopulation popofworld : populationOfWorld) {
            idnum += 1;
            if (popofworld == null)
                continue;
            sb.append("| " + idnum + "| " + numFormat.format(Long.parseLong(popofworld.getWorldPopulation())) + " | " + "|\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + populationOfWorldReport)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 27.The population of the continent.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<PeoplePopulation> getContinentPopulation(String inputTotalCont)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 27.The population of the continent.
            String strQueryTwentySeven =
                    "SELECT country.Continent, SUM(country.Population) AS 'Population' FROM country WHERE country.Continent = '"+inputTotalCont+"';";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryTwentySeven);
            // Extract continent information
            ArrayList<PeoplePopulation> populationConti = new ArrayList<PeoplePopulation>();
            while (rset.next())
            {
                PeoplePopulation popContinent = new PeoplePopulation();
                popContinent.setContinentName(rset.getString("Continent"));
                popContinent.setContinentPopulation(rset.getString("Population"));
                populationConti.add(popContinent);
            }
            return populationConti;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the people population of Asia.");
            return null;
        }
    }
    /**
     * 27. The population of the continent.
     * Formatting the output data from the list.
     **/
    public void printContinentPopulation(ArrayList<PeoplePopulation> popConti)
    {

        // Check continent is not null
        if (popConti == null) {
            System.out.println("There is no people population of Asia");
            return;
        }
        // Print header
        System.out.println(String.format("%-40s %-40s", "Continent Name", "Total Continent Population"));
        // Loop over people population in continent
        for (PeoplePopulation continentPopulation : popConti)
        {
            //print the list to check if people population in continent is null
            if (continentPopulation == null)
                continue;
            String pcontString =
                    String.format("%-40s %-40s",
                            continentPopulation.getContinentName(), continentPopulation.getContinentPopulation());
            System.out.println(pcontString);
        }
    }
    /**
     * Outputs to Markdown
     * 27. The population of the continent.
     * @param populationOfContinent
     */
    public void outputPopulationOfContinent(ArrayList<PeoplePopulation> populationOfContinent, String populationOfContinentReport) {
        // Check people population is not null
        if (populationOfContinent == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int idnum = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | Continent Name | Total Continent Population |\r\n");
        sb.append("| --- | --- | --- |\r\n");
        // Loop over continent people population in the list
        for (PeoplePopulation popofcontinent : populationOfContinent) {
            idnum += 1;
            if (popofcontinent == null)
                continue;
            sb.append("| " + idnum + "| " + popofcontinent.getContinentName() + " | " +
                    numFormat.format(Long.parseLong(popofcontinent.getContinentPopulation())) + " | " + "|\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + populationOfContinentReport)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 28.The population of the regions.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<PeoplePopulation> getRegionsPopulation(String inputTotalRegion)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 27.The population of the region.
            String strQueryTwentyEight =
                    "SELECT country.Region, SUM(country.Population) AS 'Population' FROM country WHERE country.Region='"+inputTotalRegion+"';";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryTwentyEight);
            // Extract people population information
            ArrayList<PeoplePopulation> populationRegi = new ArrayList<PeoplePopulation>();
            while (rset.next())
            {
                PeoplePopulation popRegi = new PeoplePopulation();
                popRegi.setRegionsName(rset.getString("Region"));
                popRegi.setRegionsPopulation(rset.getString("Population"));
                populationRegi.add(popRegi);
            }
            return populationRegi;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the people population of Caribbean.");
            return null;
        }
    }
    /**
     * 28. The population of the regions.
     * Formatting the output data from the list.
     **/
    public void printRegionsPopulation(ArrayList<PeoplePopulation> popRegs)
    {

        // Check region is not null
        if (popRegs == null) {
            System.out.println("There is no people population of Caribbean.");
            return;
        }
        // Print header
        System.out.println(String.format("%-40s %-40s", "Region Name", "Total Region Population"));
        // Loop over people population in region
        for (PeoplePopulation regionsPopulation : popRegs)
        {
            //print the list to check if people population in region is null
            if (regionsPopulation == null)
                continue;
            String pregiString =
                    String.format("%-40s %-40s",
                            regionsPopulation.getRegionsName(), regionsPopulation.getRegionsPopulation());
            System.out.println(pregiString);
        }
    }
    /**
     * Outputs to Markdown
     * 28. The population of the regions.
     * @param populationOfRegion
     */
    public void outputPopulationOfRegion(ArrayList<PeoplePopulation> populationOfRegion, String populationOfRegionReport) {
        // Check people population is not null
        if (populationOfRegion == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int idnum = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | Region Name | Total Region Population |\r\n");
        sb.append("| --- | --- | --- |\r\n");
        // Loop over region people population in the list
        for (PeoplePopulation popofregion : populationOfRegion) {
            idnum += 1;
            if (popofregion == null) continue;
            sb.append("| " + idnum + "| " + popofregion.getRegionsName() + " | " +
                    numFormat.format(Integer.parseInt(popofregion.getRegionsPopulation())) + " | " + "|\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + populationOfRegionReport)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 29.The population of the country.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<PeoplePopulation> getCountriesPopulation(String inputTotalCountry)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 29.The population of the country.
            String strQueryTwentyNine =
                    "SELECT country.Name, country.Population FROM country WHERE country.Name='"+inputTotalCountry+"';";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryTwentyNine);
            // Extract people population information
            ArrayList<PeoplePopulation> populCountry = new ArrayList<PeoplePopulation>();
            while (rset.next())
            {
                PeoplePopulation pCountry = new PeoplePopulation();
                pCountry.setCountriesName(rset.getString("Name"));
                pCountry.setCountriesPopulation(rset.getString("Population"));
                populCountry.add(pCountry);
            }
            return populCountry;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the people population of Austria.");
            return null;
        }
    }
    /**
     * 29. The population of the countries.
     * Formatting the output data from the list.
     **/
    public void printCountriesPopulation(ArrayList<PeoplePopulation> popContr)
    {

        // Check country is not null
        if (popContr == null) {
            System.out.println("There is no people population of Austria.");
            return;
        }
        // Print header
        System.out.println(String.format("%-50s %-40s", "Country Name", "Total Country Population"));
        // Loop over people population in country
        for (PeoplePopulation countrPopulation : popContr)
        {
            //print the list to check if people population in country is null
            if (countrPopulation == null)
                continue;
            String pcoString =
                    String.format("%-50s %-40s",
                            countrPopulation.getCountriesName(), countrPopulation.getCountriesPopulation());
            System.out.println(pcoString);
        }
    }
    /**
     * Outputs to Markdown
     * 29. The population of the countries.
     * @param populationOfCountry
     */
    public void outputPopulationOfCountry(ArrayList<PeoplePopulation> populationOfCountry, String populationOfCountryReport) {
        // Check people population is not null
        if (populationOfCountry == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int idnum = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | Country Name | Total Country Population |\r\n");
        sb.append("| --- | --- | --- |\r\n");
        // Loop over country people population in the list
        for (PeoplePopulation popofcountry : populationOfCountry) {
            idnum += 1;
            if (popofcountry == null) continue;
            sb.append("| " + idnum + "| " + popofcountry.getCountriesName() + " | " +
                    numFormat.format(Integer.parseInt(popofcountry.getCountriesPopulation())) + " | " + "|\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + populationOfCountryReport)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 30.The population of the district.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<PeoplePopulation> getDistrictPopulation(String inputTotalDistrict)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 29.The population of the country.
            String strQueryThirty =
                    "SELECT city.District, city.Population FROM city WHERE city.District='"+inputTotalDistrict+"';";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryThirty);
            // Extract people population information
            ArrayList<PeoplePopulation> populationDistr = new ArrayList<PeoplePopulation>();
            while (rset.next())
            {
                PeoplePopulation pDistrict = new PeoplePopulation();
                pDistrict.setDistrictName(rset.getString("District"));
                pDistrict.setDistrictPopulation(rset.getString("Population"));
                populationDistr.add(pDistrict);
            }
            return populationDistr;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the people population of Kabol.");
            return null;
        }
    }
    /**
     * 30. The population of the districts.
     * Formatting the output data from the list.
     **/
    public void printDistrictsPopulation(ArrayList<PeoplePopulation> popDist)
    {

        // Check district is not null
        if (popDist == null) {
            System.out.println("There is no people population of Kabol.");
            return;
        }
        // Print header
        System.out.println(String.format("%-40s %-40s", "District Name", "Total District Population"));
        // Loop over people population in district
        for (PeoplePopulation dstPopulation : popDist)
        {
            //print the list to check if people population in district is null
            if (dstPopulation == null)
                continue;
            String pdiString =
                    String.format("%-40s %-40s",
                            dstPopulation.getDistrictName(), dstPopulation.getDistrictPopulation());
            System.out.println(pdiString);
        }
    }
    /**
     * Outputs to Markdown
     * 30. The population of the districts.
     * @param populationOfDistrict
     */
    public void outputPopulationOfDistrict(ArrayList<PeoplePopulation> populationOfDistrict, String populationOfDistrictReport) {
        // Check people population is not null
        if (populationOfDistrict == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int idnum = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | District Name | Total District Population |\r\n");
        sb.append("| --- | --- | --- |\r\n");
        // Loop over district people population in the list
        for (PeoplePopulation popofdistrict : populationOfDistrict) {
            idnum += 1;
            if (popofdistrict == null) continue;
            sb.append("| " + idnum + "| " + popofdistrict.getDistrictName() + " | " +
                    numFormat.format(Integer.parseInt(popofdistrict.getDistrictPopulation())) + " | " + "|\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + populationOfDistrictReport)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 31.The population of the city.
     * Query execution and pass the array list to format the return value.
     * Function is called in main.
     **/
    public ArrayList<PeoplePopulation> getCityPopulation(String inputTotalCity)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 31.The population of the city.
            String strQueryThirtyOne =
                    "SELECT city.Name, city.Population FROM city WHERE city.Name='"+inputTotalCity+"';";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryThirtyOne);
            // Extract people population information
            ArrayList<PeoplePopulation> populatCities = new ArrayList<PeoplePopulation>();
            while (rset.next())
            {
                PeoplePopulation poCity   = new PeoplePopulation();
                poCity.setCityName(rset.getString("Name"));
                poCity.setCityPopulation(rset.getString("Population"));
                populatCities.add(poCity);
            }
            return populatCities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get the people population of Haag.");
            return null;
        }
    }
    /**
     * 31. The population of the cities.
     * Formatting the output data from the list.
     **/
    public void printCityPopulation(ArrayList<PeoplePopulation> popCity)
    {

        // Check city is not null
        if (popCity == null) {
            System.out.println("There is no people population of Haag.");
            return;
        }
        // Print header
        System.out.println(String.format("%-40s %-40s", "City Name", "Population"));
        // Loop over people population in city
        for (PeoplePopulation cityPopul : popCity)
        {
            //print the list to check if people population in city is null
            if (cityPopul == null)
                continue;
            String pcitString =
                    String.format("%-40s %-40s",
                            cityPopul.getCityName(), cityPopul.getCityPopulation());
            System.out.println(pcitString);
        }
    }
    /**
     * Outputs to Markdown
     * 31. The population of the cities.
     * @param populationOfCity
     */
    public void outputPopulationOfCity(ArrayList<PeoplePopulation> populationOfCity, String populationOfCityReport) {
        // Check people population is not null
        if (populationOfCity == null) {
            System.out.println("Error !");
            return;
        }
        NumberFormat numFormat = NumberFormat.getInstance(Locale.US);
        int idnum = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | City Name | Total City Population |\r\n");
        sb.append("| --- | --- | --- |\r\n");
        // Loop over city people population in the list
        for (PeoplePopulation popofcity : populationOfCity) {
            idnum += 1;
            if (popofcity == null) continue;
            sb.append("| " + idnum + "| " + popofcity.getCityName() + " | " +
                    numFormat.format(Integer.parseInt(popofcity.getCityPopulation())) + " | " + "|\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + populationOfCityReport)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 32. List the population of people who speak different languages in descending order
     * Query execution by user input and pass the array list to format the return value.
     * Function is called in ma
     **/
    public ArrayList<CountryLanguage> getCountryLanguage(String inputCh,String inputEn,String inputHin,String inputSp, String inputAr)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            //Query 33. List the population of people who speak different language in descending order
            String strQueryLanguage4 =
                    "SELECT countrylanguage.Language, SUM(countrylanguage.Percentage*country.Population)/100 AS countrypopulation, " +
                            "SUM(country.Population*countrylanguage.Percentage)/(SELECT SUM(country.Population) FROM country) " +
                            "AS AllCountriesPopulation FROM countrylanguage, country WHERE countrylanguage.CountryCode=country.Code " +
                            "AND countrylanguage.Language in ('"+inputCh+"', '"+inputEn+"', '"+inputHin+"', '"+inputSp+"', '"+inputAr+"') " +
                            "GROUP BY countrylanguage.Language ORDER BY AllCountriesPopulation DESC;";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strQueryLanguage4);
            // Extract language information
            ArrayList<CountryLanguage> countryLanguage4 = new ArrayList<CountryLanguage>();
            while (rset.next())
            {
                CountryLanguage language4 = new CountryLanguage();
                language4.setCountryLanguage(rset.getString("Language"));
                language4.setCountryPopulation(rset.getString("countrypopulation"));
                language4.setCountryLanguagePercent(rset.getString("AllCountriesPopulation"));
                countryLanguage4.add(language4);
            }
            return countryLanguage4;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get list the population of people who speak different language in descending order.");
            return null;
        }
    }

    /**
     * 32. List the population of people who speak different languages in descending order
     * Formatting the output data from the list.
     **/
    public void printCountryLanguage(ArrayList<CountryLanguage> countryLang)
    {
        // Check country language is not null
        if (countryLang == null)
        {
            System.out.println("There is no list the population of people who speak different language in descending order.");
            return;
        }
        // Print header
        System.out.println(String.format("%-30s %-30s %-30s", "Language", "Population", "Percent"));
        // Loop over all languages in the list
        String pattern="###.00";
        DecimalFormat df=new DecimalFormat(pattern);
        for (CountryLanguage cl4 : countryLang)
        {
            //print language to check if a language is null
            if (cl4 == null)
                continue;

            float respercent = Float.parseFloat(cl4.getCountryLanguagePercent());
            String formatrespercent = String.format(df.format(respercent));
            String resStr = formatrespercent+"%";
            cl4.setCountryLanguagePercent(resStr);

            String langString =
                    String.format("%-30s %-30s %-30s",
                            cl4.getCountryLanguage(),cl4.getCountryPopulation(), resStr);
            System.out.println(langString);
        }
    }
    /**
     * Outputs to Markdown
     * 32. List the population of people who speak different languages in descending order
     * @param populationOfLanguage
     */
    public void outputPopulationOfLanguage(ArrayList<CountryLanguage> populationOfLanguage, String populationOfLanguageReport) {
        // Check language is not null
        if (populationOfLanguage == null) {
            System.out.println("Error !");
            return;
        }
        int idnum = 0;
        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| No. | Language | Percentage |\r\n");
        sb.append("| --- | --- | --- |\r\n");
        // Loop over language in the list
        for (CountryLanguage popoflan : populationOfLanguage) {
            idnum += 1;
            if (popoflan == null) continue;
            sb.append("| " + idnum + "| " + popoflan.getCountryLanguage() + " | " + popoflan.getCountryLanguagePercent() + "|\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + populationOfLanguageReport)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args)
    {
        // Create new Application
        App a = new App();

        // Connect to database
        //a.connect();
        if(args.length < 1){
            a.connect("localhost:33060", 30000);
        }else{
            a.connect(args[0], Integer.parseInt(args[1]));
        }
        System.out.println("\n");
        // Display all the countries in the world organised by largest population to smallest.
        System.out.println("1: All the countries in the world organised by largest population to smallest.\n");
        ArrayList<Country> country = a.getAllCountries();
        a.printAllCountries(country);
        a.outputCountries(country, "All Countries in the World.md");
        System.out.println("\n");


        // Display all the countries in a continent organised by largest population to smallest.
        System.out.println("2: All the countries in Oceania organised by largest population to smallest.\n");
        ArrayList<Country> continents = a.getAllContinents("Oceania");
        a.printAllCountries(continents);
        a.outputContinent(continents, "All Countries in a Continent.md");
        System.out.println("\n");

        // Display all the countries in a region organised by largest population to smallest.
        System.out.println("3: All the countries in Caribbean organised by largest population to smallest.\n");
        ArrayList<Country> regions = a.getAllRegion("Caribbean");
        a.printAllCountries(regions);
        a.outputRegion(regions, "All Countries in a Region.md");
        System.out.println("\n");

        // Display the top N populated countries in the world where N is provided by the user.
        System.out.println("4: The top 10 populated countries in the world.\n");
        ArrayList<Country> nPopulatedCountries = a.getAllNPopulatedCountries(10);
        a.printNPopulatedCountries(nPopulatedCountries);
        a.outputRegion(nPopulatedCountries, "Top 10 Country in the World.md");
        System.out.println("\n");

        // Display all the countries in a continent organised by largest population to smallest.
        System.out.println("5. The top 10 populated countries in Europe.\n");
        ArrayList<Country> nPopulatedContinents = a.getAllNPopulatedContinents("Europe", 10);
        a.printContinent(nPopulatedContinents);
        a.outputRegion(nPopulatedContinents, "Top 10 Country in a Continent.md");
        System.out.println("\n");

        // Display the top N populated countries in a region where N is provided by the user.
        System.out.println("6: The top 10 populated countries in Caribbean.\n");
        ArrayList<Country> nPopulatedRegion = a.getAllNPopulatedRegion("Caribbean", 10);
        a.printNPopulatedRegion(nPopulatedRegion);
        a.outputRegion(nPopulatedRegion, "Top 10 Country in a Region.md");
        System.out.println("\n");

        // Display all the cities in the world organised by largest population to smallest.
        System.out.println("7: All the cities in the world organised by largest population to smallest.\n");
        ArrayList<City> cou = a.getAllCities();
        a.printCities(cou);
        a.outputCities(cou,"Cities in world.md");
        System.out.println("\n");

        // Display all the cities in a continent organised by largest population to smallest.
        System.out.println("8. All the cities in Asia organised by largest population to smallest.\n");
        ArrayList<City> continent = a.getAllCitiesContinent("Asia");
        a.printContinents(continent);
        a.outputcitycontinent(continent,"All Cities in a Continent.md");
        System.out.println("\n");

        // Display all the cities in a region organised by largest population to smallest.
        System.out.println("9: All the cities in Caribbean organised by largest population to smallest.\n");
        ArrayList<City> regi = a.getAllCitiesRegions("Caribbean");
        a.printRegions(regi);
        a.outputcityregion(regi,"All Cities in a Region.md");
        System.out.println("\n");

        // Display all the cities in a country organised by largest population to smallest.
        System.out.println("10: All the cities in Myanmar organised by largest population to smallest.\n");
        ArrayList<City> countries = a.getAllCitiesCountries("Myanmar");
        a.printCountries(countries);
        a.outputcityCountry(countries,"All Cities in a Country.md");
        System.out.println("\n");

        // Diaplay all the cities in a country organised by largest population to smallest.
        System.out.println("11: All the cities in Queensland organised by largest population to smallest.\n");
        ArrayList<City> dist = a.getAllCitiesDistrict("Queensland");
        a.printDistrict(dist);
        a.outputcityDistrict(dist,"All Cities in a District.md");
        System.out.println("\n");

        // Display the top N populated cities in the world where N is provided by the user.
        System.out.println("12: The top 10 populated cities in the world.\n");
        ArrayList<City> city = a.getTopNPopulatedCities(10);
        a.printTopNWorlds(city);
        a.outputTopNcityworld(city,"Top 10 Cities in the World.md");
        System.out.println("\n");

        // Display the top N populated cities in a continent where N is provided by the user.
        System.out.println("13. The top 10 populated cities in Europe.\n");
        ArrayList<City> topcnt = a.getTopNPopulatedContinent("Europe",10);
        a.printTopNContinent(topcnt);
        a.outputTopNcityCont(topcnt,"Top 10 Cities in a Continent.md");
        System.out.println("\n");

        // Display the top N populated cities in a region where N is provided by the user.
        System.out.println("14: The top 10 populated cities in Caribbean.\n");
        ArrayList<City> regs = a.getTopNPopulatedRegion("Caribbean",10);
        a.printTopNRegion(regs);
        a.outputTopNcityReg(regs,"Top 10 Cities in a Region.md");
        System.out.println("\n");

        // Display the top N populated cities in a country where N is provided by the user.
        System.out.println("15: The top 10 populated cities in Argentina.\n");
        ArrayList<City> ctys = a.getTopNPopulatedCountries("Argentina",10);
        a.printTopNCountries(ctys);
        a.outputTopNcitycty(ctys,"Top 10 Cities in a Country.md");
        System.out.println("\n");

        // Display the top N populated cities in a district where N is provided by the user.
        System.out.println("16: The top 10 populated cities in Zuid-Holland.\n");
        ArrayList<City> district = a.getTopNPopulatedDistrict("Zuid-Holland",10);
        a.printTopNDistrict(district);
        a.outputTopNcitydist(district,"Top 10 Cities in a District.md");
        System.out.println("\n");

        // All the capital cities in the world organised by largest population to smallest.
        System.out.println("17: All the capital cities in the world organised by largest population to smallest.\n");
        ArrayList<CapitalCity> capitalCities = a.getAllCapitalCities();
        a.printAllCapitalCities(capitalCities);
        a.outputCapitalCity(capitalCities, "All Capital Cities in the World.md");
        System.out.println("\n");

        // All the capital cities in a continent organised by largest population to smallest.
        System.out.println("18: All the capital cities in Oceania continent organised by largest population to smallest.\n");
        ArrayList<CapitalCity> capCitContinent = a.getAllCapitalCitiesContinents("Asia");
        a.printAllCapitalCityContinent(capCitContinent);
        a.outputCapitalCityCont(capCitContinent, "All Capital Cities in a Continent.md");
        System.out.println("\n");

        // All the capital cities in a region organised by largest to smallest.
        System.out.println("19.All the capital cities in Caribbean region organised by largest to smallest.\n");
        ArrayList<CapitalCity> capCitRegion = a.getAllCapitalCitiesRegions("Caribbean");
        a.printAllCapitalCityRegion(capCitRegion);
        a.outputCapitalCityReg(capCitRegion, "All Capital Cities in a Region.md");
        System.out.println("\n");

        // The top N populated capital cities in the world where N is provided by the user.
        System.out.println("20.The top 10 populated capital cities in the world \n");
        ArrayList<CapitalCity> capWld = a.getTopNCapCitiesWorld(10);
        a.printTopNCapCitiesWorld(capWld);
        a.outputTop10CapitalCity(capWld, "Top 10 Capital Cities in the World.md");
        System.out.println("\n");

        // The top N populated capital cities in a continent where N is provided by the user.
        System.out.println("21.The top 10 populated capital cities in North America \n");
        ArrayList<CapitalCity> contCapital = a.getTopNCapCitiesCont("North America",10);
        a.printTopNCapCitiesCont(contCapital);
        a.outputTop10CapitalCityCont(contCapital, "Top 10 Capital Cities in a Continent.md");
        System.out.println("\n");

        // The top N populated capital cities in a region where N is provided by the user.
        System.out.println("22.The top 10 populated capital cities in Middle East \n");
        ArrayList<CapitalCity> regWld = a.getTopNCapCitiesReg("Middle East",10);
        a.printTopNCapCitiesReg(regWld);
        a.outputTop10CapitalCityReg(regWld,"Top 10 Capital Cities in a Region.md");
        System.out.println("\n");

        // The population of people, people living in cities, and people not living in cities in each continent.
        System.out.println("23.The population of people, people living in cities, and people not living in cities in each continent. \n");
        ArrayList<PeoplePopulation> popCont = a.getPopulatedPeopleContinent();
        a.printPopulatedPeopleConitnent(popCont);
        a.outputPopulationContinent(popCont, "People Population in each Continent.md");
        System.out.println("\n");

        // The population of people, people living in cities, and people not living in cities in each region.
        System.out.println("24.The population of people, people living in cities, and people not living in cities in each region. \n");
        ArrayList<PeoplePopulation> popReg = a.getPopulatedPeopleRegions();
        a.printPopulatedPeopleRegions(popReg);
        a.outputPopulationRegion(popReg, "People Population in each Region.md");
        System.out.println("\n");

        // The population of people, people living in cities, and people not living in cities in each country.
        System.out.println("25.The population of people, people living in cities, and people not living in cities in each country. \n");
        ArrayList<PeoplePopulation> popCoun = a.getPopulatedPeopleCountry();
        a.printPopulatedPeopleCountry(popCoun);
        a.outputPopulationCountry(popCoun, "People Population in each Country.md");
        System.out.println("\n");

        // The population of the world.
        System.out.println("26. The population of the world.");
        ArrayList<PeoplePopulation> popWorld = a.getWorldPopulation();
        a.printWorldPopulation(popWorld);
        a.outputPopulationOfWorld(popWorld, "Total Population Of the World.md");
        System.out.println("\n");

        // The population of the continent.
        System.out.println("27. The population of Asia.");
        ArrayList<PeoplePopulation> popContin = a.getContinentPopulation("Asia");
        a.printContinentPopulation(popContin);
        a.outputPopulationOfContinent(popContin, "Total Population of a Continent.md");
        System.out.println("\n");

        // The population of the regions.
        System.out.println("28. The population of Caribbean.");
        ArrayList<PeoplePopulation> popRegions = a.getRegionsPopulation("Caribbean");
        a.printRegionsPopulation(popRegions);
        a.outputPopulationOfRegion(popRegions, "Total Population of a Region.md");
        System.out.println("\n");

        // The population of the countries.
        System.out.println("29. The population of Austria.");
        ArrayList<PeoplePopulation> popCountries = a.getCountriesPopulation("Austria");
        a.printCountriesPopulation(popCountries);
        a.outputPopulationOfCountry(popCountries, "Total Population of a Country.md");
        System.out.println("\n");

        // The population of the districts.
        System.out.println("30. The population of Kabol.");
        ArrayList<PeoplePopulation> popDisct = a.getDistrictPopulation("Kabol");
        a.printDistrictsPopulation(popDisct);
        a.outputPopulationOfDistrict(popDisct, "Total Population of a District.md");
        System.out.println("\n");

        // The population of the cities.
        System.out.println("31. The population of Haag.");
        ArrayList<PeoplePopulation> popCities = a.getCityPopulation("Haag");
        a.printCityPopulation(popCities);
        a.outputPopulationOfCity(popCities, "PopulationOfCity.md");
        System.out.println("\n");

        // List the population of people who speak language in descending order.
        System.out.println("32: List the population of people who speak different language in descending order.\n");
        ArrayList<CountryLanguage> countLanguage = a.getCountryLanguage("Chinese", "English","Hindi", "Spanish", "Arabic");
        a.printCountryLanguage(countLanguage);
        a.outputPopulationOfLanguage(countLanguage, "List of Different Languages Spoken.md");

        // Disconnect from database
        a.disconnect();
    }
}
