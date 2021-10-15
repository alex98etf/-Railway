package model.stanica;

import main.Zeljeznica;
import model.kompozicija.Kompozicija;


public final class Kondukter{
    private Kondukter(){}

    /**
     *
     * @param linija koje se promjerava
     * @return true - ako je linija ispravna; false - ako je linija ne isparvana
     */
    public static boolean checkLinija(String[] linija){
        for(int index = 0; index < linija.length-1; index++){
            if(getKortinate(linija[index],linija[index+1]) == null){
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param polazna Stanica
     * @param odredisna Stanica
     * @return string niz oblika [x , y , smjer]
     */
    public static String[] getKortinate(String polazna, String odredisna){

        switch (polazna) {
            case "A":
                switch (odredisna) {
                    case "B":
                        return new String[]{"27", "2", "u"};
                    case "O":
                        return new String[]{"28", "2", "d"};
                    default:
                        return null;
                }

            case "B":
                switch (odredisna) {
                    case "A":
                        return new String[]{"6", "6", "l"};
                    case "C":
                        return new String[]{"6", "7", "r"};
                    default:
                        return null;
                }

            case "C":
                switch (odredisna) {
                    case "B":
                        return new String[]{"12", "19", "u"};
                    case "D":
                        return new String[]{"12", "20", "r"};
                    case "E":
                        return new String[]{"13", "20", "d"};
                    default:
                        return null;
                }

            case "D":
                if ("C".equals(odredisna))
                    return new String[]{"1", "27", "l"};
                return null;

            case "E":
                switch (odredisna) {
                    case "C":
                        return new String[]{"25", "26", "u"};
                    case "O":
                        return new String[]{"25", "26", "r"};
                    default:
                        return null;
                }

            case "O":
                switch (odredisna) {
                    case "A":
                        return new String[]{"29", "2", "u"};
                    case "E":
                        return new String[]{"25", "29", "l"};
                    default:
                        return null;
                }

            default:
                return null;
        }
    }

    /**
     *
     * @param polazna Stanica
     * @param odredisna Stanica
     * @return Vraca pocetne kordinate odakle kompomzicija treba da krene
     */
    public static String[] postavi(Kompozicija kompozicija, String polazna, String odredisna){
        // Pronalazenje stanica
        Stanica polaznaStanica = Zeljeznica.stanice.stream().filter(stanica -> polazna.equals(stanica.getStanicaName())).findAny().orElse(null);
        Stanica odredisnaStanica = Zeljeznica.stanice.stream().filter(stanica -> odredisna.equals(stanica.getStanicaName())).findAny().orElse(null);

        if(polaznaStanica != null && odredisnaStanica != null){
            String[] kordinate = getKortinate(polazna, odredisna);
            assert kordinate != null;

            // Postavljanje linija u odgovarajuce stanice
            polaznaStanica.addOdlasci(odredisna);
            odredisnaStanica.addDolazak(polazna);

            // Dodavanje kompozicije
            polaznaStanica.addKompoziciju(kompozicija);
            return kordinate;
        }
        return null;
    }

    /**
     * Koristi se za usmejravanje kompomzicije
     * @param prethodna Stanica
     * @param trenutna Stanica
     * @param sljedeca Stanica
     * @return Vraca kordinate odakle kompozicija treba da krene
     */
    public static String[] prebaciLiniju(Kompozicija kompozicija,String prethodna, String trenutna, String sljedeca){
        // Pronalazenje stanica
        Stanica prethodanaStanica = Zeljeznica.stanice.stream().filter(stanica -> prethodna.equals(stanica.getStanicaName())).findAny().orElse(null);
        Stanica trenutnaStanica = Zeljeznica.stanice.stream().filter(stanica -> trenutna.equals(stanica.getStanicaName())).findAny().orElse(null);
        Stanica sljedecaStanica = Zeljeznica.stanice.stream().filter(stanica -> sljedeca.equals(stanica.getStanicaName())).findAny().orElse(null);

        if(prethodanaStanica != null &&trenutnaStanica != null && sljedecaStanica != null){
            // Uklanjanje prethodne linije
            prethodanaStanica.removeOdlasci(trenutna);
            trenutnaStanica.removeDolazak(prethodna);

            // Uklanjanje kompozicije
            prethodanaStanica.removeKompoziciju(kompozicija);

            // Ako ima kompozicija iza nas sa samnjenom brzino onda neka ubrza
            prethodanaStanica.revertSpeed(trenutna);

            // Postavljanje nove linije i vracanje pocetnih kordinata
            return postavi(kompozicija,trenutna,sljedeca);
        }
        return null;
    }

    /**
     * Koristi se kaka kompozicija stigne na odrediste(posljenja stanica)
     * @param polazna Stanica
     * @param odredisna Stanica
     */
    public static void ukloniLiniju(Kompozicija kompozicija,String polazna, String odredisna){
        // Pronalazenje stanica
        Stanica polaznaStanica = Zeljeznica.stanice.stream().filter(stanica -> polazna.equals(stanica.getStanicaName())).findAny().orElse(null);
        Stanica odredisnaStanica = Zeljeznica.stanice.stream().filter(stanica -> odredisna.equals(stanica.getStanicaName())).findAny().orElse(null);
        if(polaznaStanica != null && odredisnaStanica != null){
            // Uklanjanje
            polaznaStanica.removeOdlasci(odredisna);
            odredisnaStanica.removeDolazak(polazna);

            polaznaStanica.removeKompoziciju(kompozicija);

            // Ako ima kompozicija iza nas sa smanjenom brzinom neka ubrza
            polaznaStanica.revertSpeed(odredisna);
        }
    }

    /**
     *
     * @param polazna Stanica
     * @param odredisna Stanica
     * @return -1 - ne mozes(sacekaj); 0 - mozes; >1 - mozes ali uspori(vrati brzinu kojom se treba kretati)
     */
    public static int mogulKrenuti(Kompozicija kompozicija,String polazna, String odredisna) {
        // Pronalazanje stanica
        Stanica polaznaStanica = Zeljeznica.stanice.stream().filter(stanica -> polazna.equals(stanica.getStanicaName())).findAny().orElse(null);
        Stanica odredisnaStanica = Zeljeznica.stanice.stream().filter(stanica -> odredisna.equals(stanica.getStanicaName())).findAny().orElse(null);

        if (polaznaStanica != null && odredisnaStanica != null){
            // Imasli dolazak sa mog odredista
            if(polaznaStanica.checkDolaske(odredisna)){
                // Da li je ta kompozicija izasla
                if(kompozicija.isuStanici() && odredisnaStanica.checkIzlaske(polazna)){
                    return -1;
                }
                else{
                    // Ima neko van stanice
                    if(kompozicija.isuStanici() && polaznaStanica.checkIzlaske(odredisna)){
                        /// jel izlazi
                        if(polaznaStanica.izlazi(odredisna)){
                            return -1;
                        }
                        else{
                            // jesam li ja prvi da krenem
                            if(kompozicija.equals(polaznaStanica.getFirstStart(odredisna))){
                                return (int)polaznaStanica.setSpeedLimit(kompozicija,odredisna)*3/2;
                            }
                            else
                                return -1;
                        }
                    }
                    // niko nije van stanice
                    else{
                        if(polazna.hashCode() > odredisna.hashCode()){
                            return 0;
                        }
                        else
                            return -1;
                    }
                }

            }
            else{
                // Imasli Odlazak za moje odrediste
                if(polaznaStanica.checkOdlaske(odredisna)) {
                    // Ima neko van stanice
                   if(kompozicija.isuStanici() && polaznaStanica.checkIzlaske(odredisna)){
                       /// jel izlazi
                       if(polaznaStanica.izlazi(odredisna)){
                           return -1;
                       }
                       else{
                           // jesam li ja prvi da krenem
                           if(kompozicija.equals(polaznaStanica.getFirstStart(odredisna))){
                               return (int)polaznaStanica.setSpeedLimit(kompozicija,odredisna)*3/2;
                           }
                           else
                               return -1;
                       }
                   }
                   // niko nije van stanice
                   else
                       // Jesam li ja prvi koji treba krenuti
                       if(kompozicija.equals(polaznaStanica.getFirstStart(odredisna))){
                           return 0;
                       }
                       else
                           return -1;
                }
                else{
                    // Slobodno kreni
                    return 0;
                }
            }
        }
        return -1;
    }
}
