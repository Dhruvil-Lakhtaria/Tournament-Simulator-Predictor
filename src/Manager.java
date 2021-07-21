
    public class Manager{
        private String name;
        private double ability;

        public static void main(String[] args) {
            System.out.println("hi hello");
        }

        Manager(String name, double ability){

            if (name == ""){
                System.out.println("No Manager name in constructor");
            }
            else if (name.contains("[^a-zA-Z ]")){
                System.out.println("Invalid characters in Manager name in constructor");
            }

            if (ability < 0){
                System.out.println("Negative ability in constructor");
            }

            this.name = name;
            this.ability = ability;
        }

        public double getAbility() {
            return ability;
        }

        public void setAbility(double ability) {

            if (ability < 0){
                System.out.println("Negative ability being set");
            }

            this.ability = ability;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {

            if (name == ""){
                System.out.println("No Manager name being set");
            }
            else if (name.contains("[^a-zA-Z ]")){
                System.out.println("Invalid characters in Manager name being set");
            }

            this.name = name;
        }

        public String toString() {
            return "Manager Name: " + this.name + "\nManager Ability: " + this.ability + "\n";
        }
    }
