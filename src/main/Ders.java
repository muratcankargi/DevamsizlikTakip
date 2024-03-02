package main;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.lang.Class.forName;

public class Ders {
    private String ders_adi;
    private String ders_kodu;
    private int toplam_kredi;
    private int teorik_devamsizlik_gun_hakki;
    private int uygulama_devamsizlik_gun_hakki;
    private Connection connection;
    private Statement statement;

    public Ders() {
        dbConnection();
    }

    public void dbConnection() {
        try {
            this.connection = DriverManager.getConnection
                    ("jdbc:sqlserver://localhost:1433;encrypt=true;trustServerCertificate=true;" +
                            "database=DevamsizlikTakip;" +
                            "integratedSecurity=true;");

        } catch (SQLException e) {
            System.out.println("Veritabanı bağlantısı başarısız: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void getDersler() throws SQLException, ClassNotFoundException {
        forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        this.statement = this.connection.createStatement();
        String query = "SELECT * FROM Dersler";
        ResultSet rs = this.statement.executeQuery(query);

        System.out.println("Id\tDers Kodu\tDers Adı");
        while (rs.next()) {
            System.out.println(rs.getString(1) + "\t"
                    + rs.getString(3) + "\t"
                    + rs.getString(2));
        }
    }

    public void getDers(int ders_id) throws SQLException {
        ResultSet rs = this.statement.executeQuery("SELECT * FROM Dersler WHERE id = " + ders_id);

        System.out.println("Ders Kodu\tKredi\tTeori Kalan Gun\t\tUygulama Kalan Gun");

        while (rs.next()) {
            this.ders_adi = rs.getString("ders_adi");
            this.ders_kodu = rs.getString("ders_kodu");
            this.toplam_kredi = rs.getInt("toplam_kredi");
            this.teorik_devamsizlik_gun_hakki = rs.getInt("teorik_devamsizlik_gun_hakki");
            this.uygulama_devamsizlik_gun_hakki = rs.getInt("uygulama_devamsizlik_gun_hakki");
        }
        String query = "SELECT TOP(1) * FROM Devamsizliklar\t\t\n" +
                "INNER JOIN Dersler ON Devamsizliklar.ders_id = Dersler.id\n" +
                "WHERE Devamsizliklar.ders_id=" + ders_id +
                "ORDER BY Devamsizliklar.id desc;";
        PreparedStatement preparedStatement = this.connection.prepareStatement(query);
        rs = preparedStatement.executeQuery();

        while (rs.next()) {
            this.teorik_devamsizlik_gun_hakki = rs.getInt("teorik_kalan_devamsizlik");
            this.uygulama_devamsizlik_gun_hakki = rs.getInt("uygulama_kalan_devamsizlik");
        }

        System.out.println(this.ders_kodu + "\t\t"
                + this.toplam_kredi + "\t\t"
                + this.teorik_devamsizlik_gun_hakki + "\t\t\t\t\t"
                + this.uygulama_devamsizlik_gun_hakki);
    }

    public boolean devamsizlikEkle(int ders_id, int secim) throws SQLException {
        String bugun = (LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        String query = "SELECT TOP(1) * FROM Devamsizliklar\n" +
                "WHERE ders_id= " + ders_id +
                "ORDER BY id DESC";
        ResultSet rs = this.statement.executeQuery(query);

        while (rs.next()) {
            this.teorik_devamsizlik_gun_hakki = rs.getInt("teorik_kalan_devamsizlik");
            this.uygulama_devamsizlik_gun_hakki = rs.getInt("uygulama_kalan_devamsizlik");
        }

        if (secim == 1) {
            this.teorik_devamsizlik_gun_hakki--;
        } else if (secim == 2) {
            this.uygulama_devamsizlik_gun_hakki--;
        } else if (secim == 3) {
            this.teorik_devamsizlik_gun_hakki--;
            this.uygulama_devamsizlik_gun_hakki--;
        }

        String query2 = "INSERT INTO Devamsizliklar" +
                "(ders_id,teorik_kalan_devamsizlik,uygulama_kalan_devamsizlik,devamsizlik_tarihi)" +
                "values(" + ders_id + "," + this.teorik_devamsizlik_gun_hakki
                + "," + this.uygulama_devamsizlik_gun_hakki + ",'" + bugun + "')";
        int result = this.statement.executeUpdate(query2);

        if (result > 0) return true;
        else return false;
    }

    public boolean dersGuncelle(int dersId, String dersAdi, String dersKodu,
                                int toplamKredi, int kalanTeori, int kalanUygulama)
            throws SQLException {
        String query = "UPDATE Dersler SET " +
                "ders_adi='" + dersAdi + "',ders_kodu='" + dersKodu + "',toplam_kredi=" + toplamKredi +
                ",teorik_devamsizlik_gun_hakki=" + kalanTeori
                + ",uygulama_devamsizlik_gun_hakki=" + kalanUygulama + "where id=" + dersId;
        int rs = this.statement.executeUpdate(query);

        if (rs > 0) return true;

        else return false;
    }

    public void devamsizlikGetir(int dersId) throws SQLException {
        ResultSet rs = this.statement.executeQuery("SELECT * FROM Devamsizliklar\n" +
                "WHERE ders_id= " + dersId +
                "ORDER BY id asc");
        System.out.println("Gun\t\t\t\tKalan Teorik\tKalan Uygulama");
        while (rs.next()) {
            System.out.println(rs.getString("devamsizlik_tarihi") + "\t\t" +
                    rs.getInt("teorik_kalan_devamsizlik") + "\t\t\t\t" +
                    rs.getInt("uygulama_kalan_devamsizlik"));
        }
    }

    public boolean dersEkle(String dersAdi, String dersKodu, int toplamKredi, int teorik_kredi, int teorik,
                            int uygulama_kredi, int uygulama) throws SQLException {
        String query = "INSERT INTO Dersler" +
                "(ders_adi,ders_kodu,kredi_teorik,kredi_uygulama,toplam_kredi," +
                "teorik_devamsizlik_gun_hakki,uygulama_devamsizlik_gun_hakki)" +
                "values('" + dersAdi + "','" + dersKodu + "'," + teorik_kredi + "," + uygulama_kredi + "," +
                toplamKredi + "," + teorik + "," + uygulama + ")";
        int result = this.statement.executeUpdate(query);
        if (result > 0) return true;
        else return false;
    }

    public String getDers_adi() {
        return ders_adi;
    }

    public String getDers_kodu() {
        return ders_kodu;
    }

    public int getToplam_kredi() {
        return toplam_kredi;
    }

    public int getTeorik_devamsizlik_gun_hakki() {
        return teorik_devamsizlik_gun_hakki;
    }

    public int getUygulama_devamsizlik_gun_hakki() {
        return uygulama_devamsizlik_gun_hakki;
    }

}