package com.project.faith.eightpuzzle;

/**
 * Created by Faith on 8/23/2017.
 */

public class GridTemplate {
    public enum GridTemplates {TEMPLATE1, TEMPLATE2, TEMPLATE3, TEMPLATE4, TEMPLATE5}

    public enum GameMode {MANUAL, DEPTHFIRSTSEARCH, BREADTHFIRSTSEARCH, UNIFORMCOSTSEARCH, GREEDYSEARCH, ASTARSEARCH}

    public static GameMode selectedMode = GameMode.MANUAL;

    public static boolean GameStateDone = false;

    public static GridTemplates selectedTemplate = GridTemplates.TEMPLATE1;

    public static int[][] template1 = new int[][] {{5, 2, 3}, {7, 6, 4}, {1, 8, 0}};
    public static int[][] template2 = new int[][] {{5, 4, 0}, {7, 2, 3}, {8, 6, 1}};
    public static int[][] template3 = new int[][] {{4, 7, 1}, {8, 5, 2}, {3, 6, 0}};
    public static int[][] template4 = new int[][] {{1, 2, 7}, {6, 3, 8}, {0, 5, 4}};
    public static int[][] template5 = new int[][] {{1, 2, 8}, {4, 0, 7}, {5, 3, 6}};
}
