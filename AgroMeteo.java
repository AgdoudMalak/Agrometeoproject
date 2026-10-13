import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class AgroMeteo {
    public static void main(String[] args) {
        System.out.println("Bienvenue dans AgroMétéo Saïss");

//experience1
        long horodatage1 = 1727769600000L;
        int nouveau_horodatage = (int) horodatage1;
        System.out.println("Valeur originale (long) : " + horodatage1);
        System.out.println("Valeur après casting (int)  : " + nouveau_horodatage);

//experience2
        int humidite_Meknes = 124;
        byte byte_Meknes = (byte) humidite_Meknes;
        System.out.println("Valeur 124 convertie en byte : " + byte_Meknes);

        int humidite_Defectueuse = 200;
        byte byte_Defectueux = (byte) humidite_Defectueuse;
        System.out.println("Valeur 200 convertie en byte : " + byte_Defectueux);

        //experience3
        long horodatageMs = 1727769600000L;
        long totalSecondes = horodatageMs / 1000L;
        long secondesParJour = 24L * 3600L; // 86 400 secondes
        long joursEcoules = totalSecondes / secondesParJour;
        long secondesRestantes = totalSecondes % secondesParJour;

        long heures = secondesRestantes / 3600L;
        long minutes = (secondesRestantes % 3600L) / 60L;
        long secondes = secondesRestantes % 60L;

        System.out.println("Horodatage brut : " + horodatageMs + " ms");
        System.out.println("Nombre de jours entiers écoulés : " + joursEcoules + " jours");
        System.out.println("Heure de la mesure (UTC) : " + heures + "h " + minutes + "m " + secondes + "s");


//partie3
        String fichier = "mesures_saiss.csv";
        int totalLignes = 0;
        int lignesAcceptees = 0;
        int lignesRejetees = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {
            String ligne = br.readLine();
            int numeroLigne = 1;

            while ((ligne = br.readLine()) != null) {
                numeroLigne++;
                totalLignes++;
                try {
                    String[] champs = ligne.split(",");
                    long horodatage = Long.parseLong(champs[0]);
                    String station = champs[1];
                    double temperature = Double.parseDouble(champs[2]);
                    int humidite = Integer.parseInt(champs[3]);
                    double precipitation = Double.parseDouble(champs[4]);
                    double vent = Double.parseDouble(champs[5]);
                    int codeEtat = Integer.parseInt(champs[6]);

                    lignesAcceptees++;
                } catch (Exception e) {
                    lignesRejetees++;
                    System.err.println("Erreur à la ligne " + numeroLigne + " : " + e.getMessage());
                }
            }

            System.out.println("Nombre de lignes lues : " + totalLignes);
            System.out.println("Nombre de lignes acceptées : " + lignesAcceptees);
            System.out.println("Nombre de lignes rejetées : " + lignesRejetees);

            if (totalLignes > 0) {
                double taux = ((double) lignesAcceptees / totalLignes) * 100.0;
                System.out.printf("Taux de lignes acceptées : %.2f%%\n", taux);
            } else {
                System.out.println("Taux de lignes acceptées : 0.00%");
            }

        } catch (IOException e) {
            System.err.println("Erreur lors de l'ouverture du fichier : " + e.getMessage());
        }

        // --- APPEL DE TOUTES LES METHODES EXTERNES ---

        // Test Partie 4 : Validation
        System.out.println("\n--- Test Partie 4 : Validation ---");
        System.out.println("Température 25.0 °C plausible ? " + estTemperaturePlausible(25.0));
        System.out.println("Humidité 60% plausible ? " + estHumiditePlausible(60));
        System.out.println("Libellé code 1 : " + getLibelleEtat(1));

        // Test Partie 5 : Indicateurs agronomiques
        System.out.println("\n--- Test Partie 5 : Indicateurs ---");
        System.out.println("25 °C en Fahrenheit : " + celsiusVersFahrenheit(25.0) + " °F");
        System.out.println("DJC (Tmax=30, Tmin=15, Tbase=10) : " + calculerDJC(30.0, 15.0, 10.0));
        System.out.println("Risque (1.5°C, 40km/h, 30%) : " + determinerNiveauRisque(1.5, 40.0, 30));

        // Test Partie 6 : Modularité
        System.out.println("\n--- Test Partie 6 : Modularité ---");
        double[] tableau = {12.5, 14.0, 16.5};
        System.out.println("Moyenne du tableau : " + calculerMoyenne(tableau));
        System.out.println("Moyenne de 10 et 20 : " + calculerMoyenne(10.0, 20.0));

        int compteur = 5;
        incrementer(compteur);
        System.out.println("Compteur après incrément : " + compteur);

        // Test Bonus : Détection de pic
        System.out.println("\n--- Test Bonus ---");
        detecterPic(20.0, 26.0, 1727769600000L, "SAISS-01");
        calculerMoyenneGlissante(15.0, 18.0, 21.0);

        // Lancement de la Partie 7 : Menu interactif (à placer en dernier)
        System.out.println();
        afficherMenuInteractif();


    }

    //partie4
    public static boolean estTemperaturePlausible(double temperature) {
        return temperature >= -10.0 && temperature <= 50.0;
    }

    public static boolean estHumiditePlausible(int humidite) {
        return humidite >= 0 && humidite <= 100;
    }

    public static String getLibelleEtat(int codeEtat) {
        switch (codeEtat) {
            case 1:
                return "Normal";
            case 2:
                return "Suspecte";
            case 3:
                return "Panne";
            default:
                return "Code inconnu";
        }
    }

    public static boolean estMesureRetenue(boolean convertible, double temperature, int humidite, int codeEtat) {
        boolean plausible = estTemperaturePlausible(temperature) && estHumiditePlausible(humidite);
        boolean pasPanne = (codeEtat != 3);
        return convertible && plausible && pasPanne;

        //partie5
    }
    public static double celsiusVersFahrenheit(double celsius) {
        return (celsius * 9.0 / 5.0) + 32.0;
    }

    public static double calculerDJC(double tMax, double tMin, double tBase) {
        double moyenne = (tMax + tMin) / 2.0;
        double djc = moyenne - tBase;
        return djc > 0 ? djc : 0;
    }

    public static String determinerNiveauRisque(double temperature, double vent, int humidite) {
        if (temperature < 2.0) {
            return "Risque de gel";
        } else if (temperature > 30.0 && vent > 35.0 && humidite < 40) {
            return "Risque d'incendie";
        } else if (temperature > 30.0) {
            return "Stress thermique";
        } else {
            return "Conditions normales";
        }
    }
    //partie6
    public static double calculerMoyenne(double[] valeurs) {
        if (valeurs == null || valeurs.length == 0) return 0.0;
        double somme = 0.0;
        for (double v : valeurs) {
            somme += v;
        }
        return somme / valeurs.length;
    }

    public static double calculerMoyenne(double a, double b) {
        return (a + b) / 2.0;
    }

    public static void incrementer(int compteur) {
        compteur++;
    }
    //partie7
    public static void afficherMenuInteractif() {
        Scanner scanner = new Scanner(System.in);
        int choix;
        do {
            System.out.println("===== AgroMétèo Saïss =====");
            System.out.println("1. Afficher le rapport d'une station");
            System.out.println("2. Afficher toutes les alertes");
            System.out.println("3. Convertir une température (°C <-> °F)");
            System.out.println("4. Afficher le bilan de qualité des données");
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");

            if (scanner.hasNextInt()) {
                choix = scanner.nextInt();
                scanner.nextLine();
                switch (choix) {
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 0:
                        break;
                    default:
                        System.err.println("Erreur : choix hors menu. Veuillez réessayer.");
                }
            } else {
                System.err.println("Erreur : saisie invalide. Veuillez entrer un nombre.");
                scanner.nextLine();
                choix = -1;
            }
        } while (choix != 0);
        scanner.close();
    }

    //Bonus
    public static void detecterPic(double temperaturePrecedente, double temperatureActuelle, long horodatage, String station) {
        if (Math.abs(temperatureActuelle - temperaturePrecedente) > 5.0) {
            System.out.println("Pic détecté pour la station " + station + " à l'horodatage " + horodatage + " : variation de " + (temperatureActuelle - temperaturePrecedente) + " °C");
        }
    }

    public static void calculerMoyenneGlissante(double t1, double t2, double t3) {
        double moyenneGlissante = (t1 + t2 + t3) / 3.0;
        System.out.printf("Moyenne glissante sur 3 mesures : %.2f °C\n", moyenneGlissante);
    }

    public static void genererFichierUnMillionLignes() {
        String fichierSortie = "mesures_saiss_1M.csv";
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(fichierSortie))) {
            writer.println("horodatage,station,temperature,humidite,precipitation,vent,code_etat");
            long horodatageBase = 1727769600000L;
            java.util.Random random = new java.util.Random();

            long debut = System.currentTimeMillis();
            for (int i = 0; i < 1_000_000; i++) {
                long horodatage = horodatageBase + (i * 3600000L);
                String station = (i % 2 == 0) ? "SAISS-01" : "MEKNES-03";
                double temperature = 10.0 + (30.0 * random.nextDouble());
                int humidite = 20 + random.nextInt(80);
                double precipitation = random.nextDouble() * 5.0;
                double vent = random.nextDouble() * 50.0;
                int codeEtat = 1 + random.nextInt(3);

                writer.println(horodatage + "," + station + "," + temperature + "," + humidite + "," + precipitation + "," + vent + "," + codeEtat);
            }
            long fin = System.currentTimeMillis();
            System.out.println("Fichier de 1 million de lignes généré en " + (fin - debut) + " ms.");
        } catch (java.io.IOException e) {
            System.err.println("Erreur lors de la génération du fichier : " + e.getMessage());
        }
    }


}