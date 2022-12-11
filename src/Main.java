import java.util.Random;
/*
**Добавить n-го игрока, Berserk, блокирует часть удара босса по себе и прибавляет
заблокированный урон к своему урону и возвращает его боссу
 */
public class Main {
    public static int bossHealth = 1700;
    public static int bossDamage = 50;
    public static String bossDefence;
    public static int[] heroesHealth = {260, 250, 240, 290, 340, 200, 200, 200};
    public static int[] heroesDamage = {20, 15, 10, 0, 3, 5, 15, 20};
    public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic", "Medic", "Golem", "Lucky", "Berserk", "Thor"};
    public static int thorsIndex;
    public static int berserksIndex;
    public static int luckysIndex;
    public static int golemsIndex;
    public static int medicIndex;
    public static int medicsHelp = 20;
    public static int roundNumber;
    public static String criticalMessage;
    public static int golemsDefence = bossDamage / 5;
    public static int lowBossDamage = bossDamage - golemsDefence;
    public static int berserksBlock = (lowBossDamage*20)/100;


    public static void main(String[] args) {

        for (int i = 0; i < heroesAttackType.length; i++) {
            if (heroesAttackType[i] == "Medic"){
                medicIndex = i;
            }
            if (heroesAttackType[i] == "Golem"){
                golemsIndex = i;
            }
            if (heroesAttackType[i] == "Lucky"){
                luckysIndex = i;
            }
            if (heroesAttackType[i] == "Berserk"){
                berserksIndex = i;
            }
            if (heroesAttackType[i] == "Thor"){
                thorsIndex = i;
            }
        }


        printStatistics();
        while (!isGameFinished()) {
            playRound();
        }
    }

    public static void playRound() {
        roundNumber++;
        chooseBossDefence();
        bossHits();
        heroesHit();
        printStatistics();
    }

    public static void chooseBossDefence() {
        Random random = new Random();
        for (int i = 0; i < heroesAttackType.length; i++) {
            int randIndex = random.nextInt(heroesAttackType.length);// 0,1,2
            if(heroesAttackType[randIndex] == heroesAttackType[medicIndex])
                continue;
            else {
                bossDefence = heroesAttackType[randIndex];
                break;
            }
        }

    }

    public static void bossHits() {
        Random random = new Random();
        boolean luckyChance = random.nextBoolean();
        boolean thorsChance = random.nextBoolean();
        for (int k = 0; k < 1; k++) {
            if (heroesHealth[thorsIndex] > 0) {
                if (thorsChance)
                    break;
            }
            if (heroesHealth[golemsIndex] > 0) {
                heroesHealth[golemsIndex] -= golemsDefence * 2;
            }

            //----------------------------------

            for (int i = 0; i < heroesHealth.length; i++) {

                if (heroesHealth[i] > 0) {
                    if (heroesHealth[i] - bossDamage < 0) {
                        heroesHealth[i] = 0;
                    } else if (i != berserksIndex) {
                        heroesHealth[i] = heroesHealth[i] - lowBossDamage;
                    } else {
                        heroesHealth[berserksIndex] -= (lowBossDamage - berserksBlock);
                    }
                }

            }
            if (heroesHealth[luckysIndex] + lowBossDamage > 0) {
                if (luckyChance) {
                    heroesHealth[luckysIndex] += lowBossDamage;
                }
            }
        }
    }

    public static void heroesHit() {

        boolean helpTo = false;
        int helpToIndex = 0;
//medic start
        for (int i=0; i < heroesHealth.length; i++) {
            if(heroesHealth[medicIndex] > 0) {
                Random random = new Random();
                int heroesIndex = random.nextInt(heroesHealth.length);
                int plus = heroesHealth[heroesIndex];
                if (plus < 100 && plus > 0 && plus != heroesHealth[medicIndex]) {
                    helpToIndex = i;
                    helpTo = true;
                }
                if (helpTo) {
                    heroesHealth[helpToIndex] += medicsHelp;
                    break;
                }
            }
        }

        for (int i = 0; i < heroesDamage.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0) {
                int damage = heroesDamage[i];
                if (heroesAttackType[i] == bossDefence) {
                    Random random = new Random();
                    int coeff = random.nextInt(9) + 2; //2,3,4,5,6,7,8,9,10
                    damage = heroesDamage[i] * coeff;
                    criticalMessage = "Critical damage: " + damage;
                }
                if (bossHealth - damage < 0) {
                    bossHealth = 0;
                } else if (heroesHealth[i] != heroesHealth[berserksIndex] ){
                    bossHealth = bossHealth - damage;
                }
                else if(heroesHealth[berserksIndex] > 0 && bossHealth > 0){
                    bossHealth = bossHealth - (heroesDamage[berserksIndex] + berserksBlock);
                }

            }
        }
    }

    public static void printStatistics() {
        System.out.println("ROUND " + roundNumber + " ------------");
        /*String defence;
        if (bossDefence == null) {
            defence = "No defence";
        } else {
            defence = bossDefence;
        }*/
        if(bossHealth < 0 )
            bossHealth=0;
        System.out.println("Boss health: " + bossHealth + " damage: " + bossDamage + " defence: " +
                (bossDefence == null ? "No defence" : bossDefence));

        for (int i = 0; i < heroesHealth.length; i++) {
            if(heroesHealth[i]<0)
                heroesHealth[i] = 0;
            System.out.println(heroesAttackType[i] + " health: " + heroesHealth[i] + " damage: " + heroesDamage[i]);
        }
        if (criticalMessage != null) {
            System.out.println(">>>> " + criticalMessage);
        }
    }

    public static boolean isGameFinished() {
        if (bossHealth <= 0) {
            System.out.println("Heroes won!!!");
            return true;
        }
        /*if (heroesHealth[0] <= 0 && heroesHealth[1] <= 0 && heroesHealth[2] <= 0) {
            System.out.println("Boss won!!!");
            return true;
        }
        return false;*/
        boolean allHeroesDead = true;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead) {
            System.out.println("Boss won!!!");
        }
        return allHeroesDead;
    }
}
