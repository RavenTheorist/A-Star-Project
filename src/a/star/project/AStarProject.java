package a.star.project;

import java.io.*;
/**
 *
 * @author Amine Elkhalsi <aminekhalsi@hotmail.com>
 */
public class AStarProject
{
    public static void main(String[] args) throws IOException
    {
        affiche();
    }
    public static void affiche() throws IOException
    {
		String ligne = "";
		String fichier = "test.txt";
		BufferedReader clavier = new BufferedReader(new InputStreamReader(
				System.in));

		BufferedReader ficTexte;
		try {
			ficTexte = new BufferedReader(new FileReader(new File(fichier)));
			if (ficTexte == null)
                        {
				throw new FileNotFoundException("Fichier non trouvé: "
						+ fichier);
			}
			do
                        {
				ligne = ficTexte.readLine();
				if (ligne != null)
                                {
					System.out.println(ligne);
				}
			} while (ficTexte != null);
			ficTexte.close();
			System.out.println("\n");
		}
                catch (FileNotFoundException e)
                {
			System.out.println(e.getMessage());
		}
                catch (IOException e)
                {
			System.out.println(e.getMessage());
		}
	}
}