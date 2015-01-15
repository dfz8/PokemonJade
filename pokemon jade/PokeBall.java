    public class PokeBall
   {
      public int myType, myTotalHealth, myCurrentHealth;	//for the different types of pokeballs, if we get to that
   
   
       public PokeBall(int type, int totalHealth, int currentHealth){
         myType = type;
         myTotalHealth = totalHealth;
         myCurrentHealth = currentHealth;
      }
      
       public PokeBall()
      {
         
      }
      
       public boolean isCaught(){
         int randomNum =(int)( Math.random() *2);
         
         //if(myCurrentHealth < randomNum * myTotalHealth * typeFactor())
            //return true;
         //return false;
         if(randomNum == 1)
         {
            System.out.println("You Caught It!!");
            return true;
         }
         else 
            return false;
      }
      
   }