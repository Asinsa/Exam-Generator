package com.example.testgenerator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * Utils - various utility things - from genRandomString onwards may be useful or relevant
 *
 * @author nahar
 */
public class Utils {

    private Random random = new Random(System.nanoTime());

    public String fileUploadQ(String prompt, int qNum) {
        String qText = "\nTitle: Question " + qNum + " \n";
        qText += "Points: 5\n";
        qText += qNum + ". Upload the Java code you wrote to solve ONE of the " + prompt
                + " questions you attempted.\n";
        qText += "^^^^\n";
        return qText;
    }

    public void generateHeader(FileWriter outFile, String filename) throws IOException {
        outFile.write("Quiz Title: CS-110 Programming 1: " + filename + "\n");
        outFile.write("Quiz Description: <ul>"
                + "<li>You MUST use your own copy of this assessment from Canvas. If you obtained this assessment document from a friend or elsewhere "
                + "then delete this copy and use your own version from Canvas. If you experience difficulties downloading the assessment, get "
                + "in contact via the emails below.</li>"
                + "<li>Online exams are open book. This means you may consult your notes, textbooks, and other resources. However, your "
                + "answers should be your own and in your own words. Copying from resources other than your notes requires referencing. "
                + "There will not be enough time to look up material for each question.</li>"
                + "<li>You must submit before the deadline. Allow some spare time for technical submission issues.</li>"
                + "<li>This assessment is designed to take 2 hours to complete (maybe a little longer to account for typing speed). "
                + "The deadline has been set to give you a longer window than necessary to allow you time to deal with technical issues.</li>"
                + "<li>This is an individual assessment. Under no circumstances are you to discuss any aspect of this assessment with "
                + "anyone - in person or online; nor are you allowed to share the exam document, ideas or solutions with others using email, social media, "
                + "instant messaging, websites, or other means, until after 19:00 GMT (as some students may have extra time). Your "
                + "attempts at these questions must be entirely your own work. There are strict penalties applied for anyone found "
                + "to have committed academic misconduct. For example, the minimum penalty for collaborating with others is to "
                + "receive a mark of 0% FOR THE MODULE, while the penalty for the commissioning of work is to be WITHDRAWN FROM THE UNIVERSITY.</li>"
                + "<li>By submitting you state that you fully understand and are complying with the University's "
                + "<a href=\"https://myuni.swansea.ac.uk/academic-life/academic-misconduct/\" target=\"_blank\" rel=\"noopener\">Academic Misconduct Policy</a>"
                + "(opens in a new window/tab)</li>"
                + "</ul>"
                + "<h3>Special Instructions</h3>"
                + "<p>Answer All Questions.</p>"
                + "<h3>Emergency Contacts</h3>"
                + "<p>in case of queries email ALL of <a href=\"mailto:n.a.harman@swansea.ac.uk\">n.a.harman@swansea.ac.uk</a>, "
                + "<a href= \"mailto:samuel.smith@swansea.ac.uk\">samuel.smith@swansea.ac.uk</a> AND "
                + "<a href= \"mailto:assessment-scienceengineering@swansea.ac.uk\">assessment-scienceengineering@swansea.ac.uk</a>.</p>"
                + "<h2>Problem Solving Questions</h2>"
                + "<p>Below are descriptions of the two Problem Solving question groups on this paper - you should refer to the background material on Canvas "
                + "in the January Assessment Information module where you can find information on each of the six possible Problem Solving question types.</p>"
                + "<p>Remember that you can copy&paste strings, numbers and fragments of code from the questions - don't try to"
                + " type them in.</p>");
    }

    /*
    public int generateMcqBlock(FileWriter outfile, String blockFileName, int numQs, int startNum, int points) {
        Scanner in = null;
        try {
            in = new Scanner(new File(blockFileName));
        } catch (FileNotFoundException e) {
            System.out.println("file not found: " + blockFileName);
            System.exit(0);
        }
        McqBlock block = new McqBlock(in, numQs);
        try {
            Mcq nextQ = block.returnNextQ();
            while (nextQ != null) {
                outfile.write("Title: Question " + startNum + "\n");
                outfile.write("Points: " + points + "\n");
                outfile.write(startNum + nextQ.getQtext() + "\n");
                String[] answers = nextQ.getAnswers();
                for (int i = 0; i < answers.length; i++) {
                    outfile.write(answers[i] + "\n");
                }
                nextQ = block.returnNextQ();
                startNum++;
            }
        } catch (Exception e) {
            System.out.println("file write failure");
            System.exit(0);
        }
        return startNum;
    }
     */

    public String genRandomString(int minLen, int sizeRng, char low, char high,
                                  boolean xYHack) {

        int targetStringLength;
        if (sizeRng == 0) {
            targetStringLength = minLen;
        } else {
            targetStringLength = random.nextInt(sizeRng) + minLen;
        }
        StringBuilder buffer = new StringBuilder(targetStringLength);
        int leftLmt = (int) low;
        int limLen = ((int) high - low);

        if (!xYHack) {
            for (int i = 0; i < targetStringLength; i++) {
                int randomLimitedInt = leftLmt + (int) (random.nextFloat() * (limLen));
                buffer.append((char) randomLimitedInt);
            }
        } else {
            for (int i = 0; i < targetStringLength; i++) {
                int xY = random.nextInt(10);
                if (xY == 0) {
                    buffer.append('Y');
                } else if (xY < 3) {
                    buffer.append('X');
                } else {
                    int randomLimitedInt = leftLmt + (int) (random.nextFloat() * (limLen));
                    buffer.append((char) randomLimitedInt);
                }
            }
        }
        return buffer.toString();
    }

    public String[] reorderQSet(String[] qSet, int n, int correctLoc) {
        if (n < 0 || n > qSet.length || correctLoc < 0 || correctLoc > qSet.length) {
            System.out.println("***REORDER CONSISTENCY ERROR***");
            return qSet;//Error here, do something better
        }
        String temp = qSet[n];
        qSet[n] = qSet[correctLoc];
        qSet[correctLoc] = temp;
        return qSet;
    }

    public void randomizeAnswers(String[] qSet, int index) {
        int middle = qSet[index].length() / 2;
        boolean notDone = true;
        do {
            for (int i = 2; i < qSet.length; i++) {
                int randomSwitch = random.nextInt(middle / 3);
                String tempStr = qSet[index];
                char[] chars = tempStr.toCharArray();
                char temp = chars[middle + randomSwitch];
                chars[middle + randomSwitch] = chars[middle - randomSwitch];
                chars[middle - randomSwitch] = temp;
                qSet[i] = String.valueOf(chars);
            }
            notDone = testNotUnique(qSet);
        } while (notDone);
    }

    public boolean testNotUnique(String[] stringVals) {
        for (int i = 0; i < stringVals.length; i++) {
            for (int j = i + 1; j < stringVals.length; j++) {
                if (i != j && stringVals[i].equals(stringVals[j])) {
                    return true;
                }
            }
        }
        return false;
    }

    public void checkEquals(String a, String b) {
        if (!a.equals(b)) {
            System.out.println("***ERRROR EQUALS***");
            System.out.println(a);
            System.out.println("should equal");
            System.out.println(b);
        }
    }

    public void checkNotEquals(String a, String b) {
        if (a.equals(b)) {
            System.out.println("***ERRROR NOT EQUALS***");
            System.out.println(a);
            System.out.println("should NOT equal");
            System.out.println(b);
        }
    }

    public int genRandomRange(int low, int high) {
        return random.nextInt(high - low + 1) + low;
    }

    public long genRandomRangeLong(long low, long high) {
        return ThreadLocalRandom.current().nextLong(low, high);
    }
}
