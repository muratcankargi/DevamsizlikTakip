package main;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        run();
    }

    public static void run() {
        Ders ders = new Ders();

        while (true) {
            Scanner input = new Scanner(System.in);
            try {
                System.out.print("\n1 - Ders Sec\n" +
                        "2 - Ders Ekle\n" +
                        "0 - Cikis\n" +
                        "Sec: ");
                int secim1 = input.nextInt();
                if (secim1 == 1) {
                    ders.getDersler();
                    System.out.print("Ders Id :");
                    int ders_id = input.nextInt();
                    ders.getDers(ders_id);
                    System.out.print("1 - Devamsızlık Ekle\n" +
                            "2 - Bilgileri Duzenle\n" +
                            "3 - Devamsizlik Gunleri\n" +
                            "0 - Cikis\n" +
                            "Sec: ");
                    int secim2 = input.nextInt();
                    if (secim2 == 1) {
                        System.out.print("1 - Teorik\n" +
                                "2 - Uygulama\n" +
                                "3 - Teorik + Uygulama\n" +
                                "0 - Cikis\n" +
                                "Sec: ");

                        int secim3 = input.nextInt();

                        if (secim3 != 0)
                            ders.devamsizlikEkle(ders_id, secim3);


                    } else if (secim2 == 2) {
                        input.nextLine();
                        System.out.print("Ders Adi (" + ders.getDers_adi() + ") : ");
                        String ders_adi = input.nextLine();
                        System.out.print("Ders Kodu (" + ders.getDers_kodu() + ") : ");
                        String ders_kodu = input.next();
                        System.out.print("Toplam kredi (" + ders.getToplam_kredi() + ") : ");
                        int toplam_kredi = input.nextInt();
                        System.out.print("Kalan teorik devamsızlik hakki (" + ders.getTeorik_devamsizlik_gun_hakki() + ") : ");
                        int kalan_teori = input.nextInt();
                        System.out.print("Kalan uygulama devamsızlik hakki (" + ders.getUygulama_devamsizlik_gun_hakki() + ") : ");
                        int kalan_uygulama = input.nextInt();

                        ders.dersGuncelle(ders_id, ders_adi, ders_kodu, toplam_kredi, kalan_teori, kalan_uygulama);

                    } else if (secim2 == 3) {
                        ders.devamsizlikGetir(ders_id);
                    }

                } else if (secim1 == 2) {
                    input.nextLine();
                    System.out.print("Ders Adi: ");
                    String ders_adi = input.nextLine();
                    System.out.print("Ders Kodu: ");
                    String ders_kodu = input.nextLine();
                    System.out.print("Toplam kredi:");
                    int toplam_kredi = input.nextInt();
                    System.out.print("Teorik Kredi:");
                    int teorik_kredi = input.nextInt();
                    System.out.print("Teorik Devamsizlik Hakki:");
                    int teorik = input.nextInt();
                    System.out.print("Uygulama Kredi:");
                    int uygulama_kredi = input.nextInt();
                    System.out.print("Uygulama Devamsizlik Hakki:");
                    int uygulama = input.nextInt();

                    ders.dersEkle(ders_adi, ders_kodu, toplam_kredi, teorik_kredi, teorik, uygulama_kredi, uygulama);
                }

            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
                System.out.println("Hatali deger girdiniz.");
            }
        }
    }
}
