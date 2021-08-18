package simulator.components;

public class Manager{
        private String name;
        private double ability;

        public Manager(String name, double ability){

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

        public String getName() {
            return name;
        }
}
