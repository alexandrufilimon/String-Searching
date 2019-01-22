import java.util.ArrayList;

public class KMP {
	private int[] array;
	public static ArrayList<Integer> matchIndex = new ArrayList<Integer>();
	public static int matchCount;

	// +++++++++++++++++++++++++++ CONSTRUCTOR +++++++++++++++++++++++++++ 
	public KMP(){}
	
	// +++++++++++++++++++++++++++ MEMBER FUNCTIONS ++++++++++++++++++++++++++++ +
	void temporaryArray(String pattern){
		int j = 0, i = 1;
		array = new int[pattern.length()];
		array[0] = 0; // este intotdeauna 0
		
		while(i < pattern.length()){
			if(pattern.charAt(j) == pattern.charAt(i)){
				array[i] = j+1;
				j++;
				i++;
			}
			else if(pattern.charAt(j) != pattern.charAt(i)){
				if(j != 0){
					j = array[j-1];
				}
				else{ 
					array[i] = j;
					i++;
				}
			}
		}
	}
	
	void searchKMP(String pattern, String text){
		if(pattern.length()-1 <= text.length()-1){
			int i = 0, j = 0;
			temporaryArray(pattern);
			
			while(i<text.length()){
				if(pattern.charAt(j) == text.charAt(i)){
					i++;
					j++;
				}
				if(j == pattern.length()){
					matchIndex.add(i-j);
					matchCount++;
					j = array[j-1];
				}
				else if(pattern.charAt(j) != text.charAt(i) && i<text.length()){
					if(j != 0){
						j = array[j-1];
					}
					else{
						i++;
					}
				}
			}
		}
		else{
			throw new RuntimeException("EROARE: Pattern-ul trebuie sa fie mai scurt decat lungimea textului.");
		}
	}
	
	
    public static void main(String args[]) 
    { 
        new UI();
    } 
}
