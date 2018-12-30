// Name: Arul Nigam
// Date: 12/16/2018

import java.util.List;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;
import java.util.*;

public class Twitter_Warmup
{
   public static void main (String []args) throws IOException
   {
      TJTwitter2 twitter = new TJTwitter2();
   
      //  testing remove punctuation
      String s1 = "abcd?";
      String s2 = "!abc$d";
      String s3 = "ab:cd..";
      String s4 = "abc'd";
      System.out.println( s1 + " without puncutation is " + twitter.removePunctuation(s1) );
      System.out.println( s2 + " without puncutation is " + twitter.removePunctuation(s2) );
      System.out.println( s3 + " without puncutation is " + twitter.removePunctuation(s3) );
      System.out.println( s4 + " without puncutation is " + twitter.removePunctuation(s4) );
   
      System.out.println();
   
      String f1 = "story.txt";
      String f2 = "test.txt";
      System.out.println("For the file: " + f1);
      twitter.queryHandle("story.txt");
      System.out.println("Most popular word: " + twitter.mostPopularWord());
      System.out.println("Frequency: " + twitter.getFrequencyMax());
      System.out.println();
   
      System.out.println("For the file: " + f2);
      twitter.queryHandle("test.txt");
      System.out.println("Most popular word: " + twitter.mostPopularWord());
      System.out.println("Frequency: " + twitter.getFrequencyMax());
   }
}

class TJ_Status2
{
   private String text;

   public TJ_Status2(String s)
   {
      text = s;
   }
   public String getText()
   {
      return text;
   }
}

class TJTwitter2
{
   private List<TJ_Status2> statuses;
   private int numberOfTweets;
   private List<String> terms;
   private String popularWord;
   private int frequencyMax;

   public TJTwitter2() throws IOException
   {
      statuses = new ArrayList<TJ_Status2>();
      terms = new ArrayList<String>();
   }

   public List<String> getTerms()
   {
      return terms;
   }

   public int getNumberOfTweets()
   {
      return numberOfTweets;
   }

   public String getMostPopularWord()
   {
      return popularWord;
   }

   public int getFrequencyMax()
   {
      return frequencyMax;
   }

   @SuppressWarnings("unchecked")
   public void queryHandle(String handle)throws IOException
   {
      statuses.clear();
      terms.clear();
      fetchTweets(handle);
      System.out.println("Number of tweets: " + getNumberOfTweets());
      splitIntoWords();
      System.out.println("All the words: " + terms);
      removeCommonEnglishWords();
      System.out.println("Remove common words: " +terms);
      sortAndRemoveEmpties();
      System.out.println("Sorted: " + terms);
      // mostPopularWord();
   }

   /**
    * This method reads a file of tweets and
    * stores them in an arrayList of TJ_Status2 objects.
    * Populates statuses.
    * @param String  the text file
    */
   public void fetchTweets(String handle) throws IOException
   {
      Scanner scan = new Scanner(new File(handle));
      while(scan.hasNext())
         statuses.add(new TJ_Status2(scan.nextLine()));
      numberOfTweets = statuses.size();
   }

   /**
    * This method takes each status and splits them into individual words.
    * Remove punctuation by calling removePunctuation, then store the word in terms.
    */
   public void splitIntoWords()
   {
      String str;
      for(int i = 0; i < statuses.size(); i++)
      {
         str = statuses.get(i).getText();
         while(str != null)
         {
            if(str.contains(" "))
            {
               terms.add(removePunctuation(str.substring(0, str.indexOf(" "))));
               str = str.substring(str.indexOf(" ")).trim();
            }
            else // last word
            {
               terms.add(removePunctuation(str.trim()));
               str = null;
            }
         }
      }
   }


   /**
    * This method removes common punctuation (but not apostrophes) from each individual word.
    * This method removes empty strings.
    * This method changes everything to lower case.
    * Consider reusing code you wrote for a previous lab.
    * Consider if you want to remove the # or @ from your words. Could be interesting to keep (or remove).
    * @ param String  the word you wish to remove punctuation from
    * @ return String the word without any punctuation, all lower case
    */
   public String removePunctuation( String s )
   {
      String ret = "";
      char[] temp = s.toCharArray();
      for(int i = 0; i < temp.length; i++)
      {
         if(Character.isLetterOrDigit(temp[i]) || temp[i] == '\'' || temp[i] == '#' || temp[i] == '@' || temp[i] == '-' )
         {
            ret += ("" + temp[i]);
         }
      }
      return ret.toLowerCase();
   }

   /**
    * This method removes common English words from the list of terms.
    * Remove all words found in commonWords.txt  from the argument list.
    * The count will not be given in commonWords.txt. You must count the number of words in this method.
    * This method should NOT throw an excpetion.  Use try/catch.
    */
   @SuppressWarnings("unchecked")
   public void removeCommonEnglishWords()
   {
      /*BEGIN COUNT OF SIZE*/
      Scanner in = null;
      int count = 0;
      try
      {
         in = new Scanner(new File("commonWords.txt"));
      }
      catch(Exception e)
      {
         System.out.println("ERROR! FILE NOT FOUND");
         System.exit(0);
      }
      while(in.hasNextLine())
      {
         count += 1;
         in.nextLine();
      }
      /*COUNT COMPLETED. BEGIN READING INTO ARRAY*/
      String[] commonWords = new String[count];
      try
      {
         in = new Scanner(new File("commonWords.txt"));
      }
      catch(Exception e)
      {
         System.out.println("ERROR! FILE NOT FOUND");
         System.exit(0);
      }
      for(int i = 0; i < commonWords.length; i++)
      {
         commonWords[i] = in.nextLine();
      }
      in.close();
      /*READING IN COMPLETED. BEGIN CLEARING TERMS?*/
      ListIterator<String> it;
      for(int i = 0; i < commonWords.length; i++)
      {
         it = terms.listIterator(); //terms or statuses?
         while(it.hasNext())
         {
            if(it.next().equalsIgnoreCase(commonWords[i]))
            {
               it.remove();
            }
         }
      }
   }

   /**
    * This method sorts the words in terms in alphabetically (and lexicographic) order.
    * You should use your sorting code you wrote earlier this year.
    * Remove all empty strings while you are at it.
    */
   @SuppressWarnings("unchecked")
   public void sortAndRemoveEmpties()
   {
      ListIterator<String> it = terms.listIterator();
      while(it.hasNext())
      {
         if(it.next().compareTo(" ") == 0)
         {
            it.remove();
         }
      }
      Comparable[] temp = new Comparable[terms.size()];
      for(int i = 0; i < terms.size(); i++)
      {
         temp[i] = terms.get(i);
      }
      sort(temp);
      for(int i = 0; i < terms.size(); i++)
      {
         terms.set(i, "" + temp[i]);
      }
   }

   @SuppressWarnings("unchecked")//this removes the warning for Comparable
   public static void sort(Comparable[] array)
   {
      helper(array, 0, array.length - 1);
   }

   @SuppressWarnings("unchecked")//this removes the warning for Comparable
   private static void helper(Comparable[] array, int first, int last)
   {
      int indexOfPivot;
      if (first < last)             // General case
      {
         indexOfPivot = rearrange(array, first, last);
         helper(array, first, indexOfPivot - 1);   // sort left side
         helper(array, indexOfPivot + 1, last);    // sort right side
      }
   }

   @SuppressWarnings("unchecked")//this removes the warning for Comparable
   private static int rearrange(Comparable[] array, int first, int last)
   {
      int indexOfPivot = first;
      Comparable pivot = array[first];
   
      while (first <= last)
      {
         if(array[first].compareTo(pivot) <= 0)       //if on correct side,
            first++;                      //   move over
         else if(array[last].compareTo(pivot) > 0)   //if on correct side,
            last--;                       //   move over
         else         // both are on the wrong side
         {
            swap(array, first, last); // then swap them, and
            first++;                       //   move over
            last--;                       //   move over
         }
      }
      swap(array, last, indexOfPivot);    // swap pivot with element at indexOfPivot
      indexOfPivot = last;                // set indexOfPivot to place where the halves meet
      return indexOfPivot;
   }

   @SuppressWarnings("unchecked")//this removes the warning for Comparable
   private static void swap(Comparable[] array, int a, int b)
   {
      Comparable temp = array[a];
      array[a] = array[b];
      array[b] = temp;
   }
   /**
    * This method returns the most common word from terms.
    * Consider case - should it be case sensitive?  The choice is yours.
    * @return String the word that appears the most times
    * @post will popopulate the frequencyMax variable with the frequency of the most common word
    */
   @SuppressWarnings("unchecked")
   public String mostPopularWord()
   {
      String pop = "";
      String popRecord = "";
      frequencyMax = 0;
      int count = 1;
      List<String> temp = terms;
      ListIterator<String> it = temp.listIterator();
      while(temp.size() > 0)
      {
         it = temp.listIterator();
         pop = it.next();
         count = 1;
         it.remove();
         while(it.hasNext())
         {
            if(pop.equalsIgnoreCase(it.next()))
            {
               count++;
               it.remove();
            }
         }
         if(count > frequencyMax)
         {
            frequencyMax = count;
            popRecord = pop;
         }
      }
      return popRecord;
   }
}

/******************************** Sample output

 abcd
 abcd
 abcd
 abc'd

 Number of tweets = 28
 All the words: [as, a, 20-year-old, pfc, in, the, air, force, oct, 27, 1949, was, a, day, , i'll, always, remember, i, was, stationed, at, chanute, field, illinois, after, finishing, basic, training, at, sheppard, air, force, base, in, texas, i, was, transferred, to, chanute, to, attend, aircraft, , engine, and, general, aircraft, training, while, on, barracks, cleanup, duty, i, found, a, copy, of, the, , vancouver, sun, newspaper, from, , british, columbia, the, front-page, article, was, about, the, pacific, national, exhibition, beauty, contest, with, a, photo, of, the, winner, miss, vancouver, the, article, also, listed, the, 11, other, contestants, and, the, cities, they, represented, well, , i, got, the, bright, idea, of, writing, a, letter, to, the, winner, hoping, to, get, some, mail, in, return, since, i, had, been, away, from, home, for, almost, a, year, the, highlight, of, my, day, was, mail, call, i, wrote, to, two, other, contestants, as, well, but, had, only, their, , cities, to, use, for, the, address, i, was, shocked, when, i, got, return, letters, from, all, three, contestants, i, was, very, impressed, with, the, letter, from, miss, port, moody, kay, ronco, and, we, began, writing, regularly, by, this, time, , i, had, finished, the, tech, school, programs, and, was, transferred, to, a, base, in, omaha, nebraska, , kay, and, i, continued, to, write, after, seven, months, i, was, made, a, crew, member, on, a, b-29, bomber, , scheduled, to, fly, to, seattle, washington, for, modification]
 Remove common words: [20-year-old, pfc, air, force, oct, 27, 1949, day, , i'll, always, remember, stationed, chanute, field, illinois, after, finishing, basic, training, sheppard, air, force, base, texas, transferred, chanute, attend, aircraft, , engine, general, aircraft, training, while, barracks, cleanup, duty, found, copy, , vancouver, sun, newspaper, , british, columbia, front-page, article, pacific, national, exhibition, beauty, contest, photo, winner, miss, vancouver, article, also, listed, 11, other, contestants, cities, represented, well, , got, bright, idea, writing, letter, winner, hoping, mail, return, since, away, home, almost, year, highlight, day, mail, call, wrote, other, contestants, well, only, , cities, address, shocked, got, return, letters, three, contestants, very, impressed, letter, miss, port, moody, kay, ronco, began, writing, regularly, time, , finished, tech, school, programs, transferred, base, omaha, nebraska, , kay, continued, write, after, seven, months, made, crew, member, b-29, bomber, , scheduled, fly, seattle, washington, modification]
 Sorted: [11, 1949, 20-year-old, 27, address, after, after, air, air, aircraft, aircraft, almost, also, always, article, article, attend, away, b-29, barracks, base, base, basic, beauty, began, bomber, bright, british, call, chanute, chanute, cities, cities, cleanup, columbia, contest, contestants, contestants, contestants, continued, copy, crew, day, day, duty, engine, exhibition, field, finished, finishing, fly, force, force, found, front-page, general, got, got, highlight, home, hoping, i'll, idea, illinois, impressed, kay, kay, letter, letter, letters, listed, made, mail, mail, member, miss, miss, modification, months, moody, national, nebraska, newspaper, oct, omaha, only, other, other, pacific, pfc, photo, port, programs, regularly, remember, represented, return, return, ronco, scheduled, school, seattle, seven, sheppard, shocked, since, stationed, sun, tech, texas, three, time, training, training, transferred, transferred, vancouver, vancouver, very, washington, well, well, while, winner, winner, write, writing, writing, wrote, year]
 Most popular word: contestants
 Frequency: 3

 Number of tweets = 6
 All the words: [this, is, a, test, to, check, mia, if, mia, it's, working, or, a, a, a, not, mia]
 Remove common words: [test, check, mia, mia, it's, working, mia]
 Sorted: [check, it's, mia, mia, mia, test, working]
 Most popular word: mia
 Frequency: 3


 *********************************************************/