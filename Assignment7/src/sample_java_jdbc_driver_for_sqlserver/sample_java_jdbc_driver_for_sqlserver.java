package sample_java_jdbc_driver_for_sqlserver;

import java.sql.*;
import java.util.Scanner;

public class sample_java_jdbc_driver_for_sqlserver {
    public static void main(String[] args) {
        String sSQLServerString = "jdbc:sqlserver://cypress.csil.sfu.ca;databaseName=";
        String sUsername = "s_xha91";
        String sPassphrase = "HQr6m4AN4FHtyTe7";
        int option = 0;
        int initial_counter = 0;
        int truefalse = 0;
        ResultSet userResultSet = null;
        String login_id;
        String friend_id;
       
        

        Scanner scanner = new Scanner(System.in);

        try {
            Connection connection = DriverManager.getConnection(sSQLServerString, sUsername, sPassphrase);
            System.out.println("Connected to Server");

            while (truefalse == 0) {
                if (initial_counter == 0) {
                    System.out.print("Enter your user ID to log in: ");
                    login_id = scanner.nextLine().trim();
                    

                    String query = "SELECT user_id FROM user_yelp WHERE user_id = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        preparedStatement.setString(1, login_id);
                        ResultSet resultSet = preparedStatement.executeQuery();

                        if (resultSet.next()) {
                            truefalse = 1;
                            System.out.println("Login successful! Welcome, " + login_id + ".");
                            int orderByChoice = 0;
                            //-------------------------------------------------------------------------------------
                            while (option == 0) {
                                System.out.print("\n");
                                System.out.print("========================================\n");
                                System.out.println("Welcome To The Main Menu, Please Select An Option Below ");
                                System.out.println("1. Search Business");
                                System.out.println("2. Search Users");
                                System.out.println("3. Make Friend");
                                System.out.println("4. Review Business");
                                System.out.println("5. Exit Program");
                                System.out.print("Please Enter Your Option Here: ");
                                String choiceinput = scanner.nextLine().trim();

                                //-------------------------------------------------------------------------------------------
                                if (choiceinput.equals("1")) {
                                    System.out.println("============================================");
                                    System.out.print("~~~Search Business~~~\n");
                                    System.out.print("Enter minimum number of stars (press Enter for no minimum): ");
                                    String starsInput = scanner.nextLine().trim();

                                    System.out.print("Enter City (press Enter for any city): ");
                                    String cityInput = scanner.nextLine().trim();

                                    System.out.print("Enter Name or part of the name (press Enter for any name): ");
                                    String nameInput = scanner.nextLine().trim();

                                    
                                    StringBuilder queryBuilder = new StringBuilder("SELECT business_id, name, address, city, postal_code, stars, review_count FROM business WHERE 1 = 1");

                                    if (!starsInput.isEmpty()) {
                                        queryBuilder.append(" AND stars >= ").append(starsInput);
                                    }

                                    if (!cityInput.isEmpty()) {
                                        queryBuilder.append(" AND city = '").append(cityInput).append("'");
                                    }

                                    if (!nameInput.isEmpty()) {
                                        queryBuilder.append(" AND LOWER(name) LIKE LOWER('%").append(nameInput).append("%')");
                                    }

                                    System.out.println("Order the results by:");
                                    System.out.println("    1. Name");
                                    System.out.println("    2. City");
                                    System.out.println("    3. Stars");
                                    System.out.print("Enter your choice: ");
                                    orderByChoice = scanner.nextInt();
                                    scanner.nextLine(); 

                                    switch (orderByChoice) {
                                        case 1:
                                            queryBuilder.append(" ORDER BY name");
                                            break;
                                        case 2:
                                            queryBuilder.append(" ORDER BY city");
                                            break;
                                        case 3:
                                            queryBuilder.append(" ORDER BY stars");
                                            break;
                                        default:
                                            System.out.println("not a valid option, order will be as they appear in table");
                                    }

                                    try (Statement statement = connection.createStatement();
                                         ResultSet Rset = statement.executeQuery(queryBuilder.toString())) {

                                        if (!Rset.isBeforeFirst()) {
                                            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~");
                                            System.out.println("\nSearch result is empty.\n");
                                            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~");
                                        } else {
                                            
                                            System.out.println("Search results:");
                                            while (Rset.next()) {
                                                String businessId = Rset.getString("business_id");
                                                String businessName = Rset.getString("name");
                                                String address = Rset.getString("address");
                                                String city = Rset.getString("city");
                                                String postalCode = Rset.getString("postal_code");

                                                
                                                double stars = Rset.getDouble("stars");

                                                int reviewCount = Rset.getInt("review_count");

                                                System.out.println("Business ID: " + businessId + ", Name: " + businessName +
                                                        ", Address: " + address + ", City: " + city +
                                                        ", Postal Code: " + postalCode + ", Stars: " + stars +
                                                        ", Review Count: " + reviewCount);
                                            }
                                        }

                                    } catch (SQLException e) {
                                        System.out.println("Failed to execute query");
                                        e.printStackTrace();
                                    }
                                }
                                //------------------------------------------------------------------------------------------

                                if (choiceinput.equals("2")) {
                                    System.out.println("============================================");
                                    System.out.print("~~~Search Users~~~\n");
                                    System.out.print("Enter part of the name (press Enter for any name): ");
                                    String userNameInput = scanner.nextLine().trim();

                                    System.out.print("Enter minimum review count (press Enter for no minimum): ");
                                    String minReviewCountInput = scanner.nextLine().trim();

                                    System.out.print("Enter minimum average stars (press Enter for no minimum): ");
                                    String minAvgStarsInput = scanner.nextLine().trim();

                                    
                                    StringBuilder userQueryBuilder = new StringBuilder("SELECT user_id, name, review_count, useful, funny, cool, average_stars, yelping_since FROM user_yelp WHERE 1 = 1");

                                    if (!userNameInput.isEmpty()) {
                                        userQueryBuilder.append(" AND LOWER(name) LIKE LOWER('%").append(userNameInput).append("%')");
                                    }

                                    if (!minReviewCountInput.isEmpty()) {
                                        userQueryBuilder.append(" AND review_count >= ").append(minReviewCountInput);
                                    }

                                    if (!minAvgStarsInput.isEmpty()) {
                                        userQueryBuilder.append(" AND average_stars >= ").append(minAvgStarsInput);
                                    }

                                    
                                    userQueryBuilder.append(" ORDER BY name");

                                    try (Statement userStatement = connection.createStatement();
                                         ResultSet findUserResultSet = userStatement.executeQuery(userQueryBuilder.toString())) {

                                        if (!findUserResultSet.isBeforeFirst()) {
                                            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~");
                                            System.out.println("\nSearch result is empty.\n");
                                            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~");
                                        } else {
                                            
                                            System.out.println("Search results for Users:");
                                            while (findUserResultSet.next()) {
                                                String userId = findUserResultSet.getString("user_id");
                                                String userName = findUserResultSet.getString("name");
                                                int reviewCount = findUserResultSet.getInt("review_count");
                                                int usefulCount = findUserResultSet.getInt("useful");
                                                int funnyCount = findUserResultSet.getInt("funny");
                                                int coolCount = findUserResultSet.getInt("cool");
                                                double avgStars = findUserResultSet.getDouble("average_stars");
                                                Date yelpingSince = findUserResultSet.getDate("yelping_since");

                                                System.out.println("User ID: " + userId + ", Name: " + userName +
                                                        ", Review Count: " + reviewCount +
                                                        ", Useful: " + usefulCount + ", Funny: " + funnyCount + ", Cool: " + coolCount +
                                                        ", Average Stars: " + avgStars + ", Yelping Since: " + yelpingSince);
                                            }
                                        }

                                    } catch (SQLException e) {
                                        System.out.println("Failed to execute query");
                                        e.printStackTrace();
                                    }
                                }
                                //------------------------------------------------------------------------------------------
                                if (choiceinput.equals("3")) {
                                    System.out.println("~~~Make Friend~~~");

                                    
                                    System.out.print("Enter the User ID of the friend you want to add: ");
                                    friend_id = scanner.nextLine().trim();

                                   
                                    String checkUserQuery = "SELECT user_id FROM user_yelp WHERE user_id = ?";
                                    boolean isValidUser = false;

                                    try (PreparedStatement checkUserStatement = connection.prepareStatement(checkUserQuery)) {
                                        checkUserStatement.setString(1, friend_id);
                                        ResultSet checkUserResultSet = checkUserStatement.executeQuery();

                                        isValidUser = checkUserResultSet.next();

                                    } catch (SQLException e) {
                                        System.out.println("Failed to execute query");
                                        e.printStackTrace();
                                    }

                                    if (!isValidUser) {
                                        System.out.println("The entered user ID is not valid. Going back to the main menu.");
                                    } else {
                                        
                                        boolean areFriends = false;
                                        String checkFriendsQuery = "SELECT * FROM Friendship WHERE user_id = ? AND friend = ?";

                                        try (PreparedStatement checkFriendsStatement = connection.prepareStatement(checkFriendsQuery)) {
                                            checkFriendsStatement.setString(1, login_id);
                                            checkFriendsStatement.setString(2, friend_id);

                                            ResultSet checkFriendsResultSet = checkFriendsStatement.executeQuery();
                                            areFriends = checkFriendsResultSet.next();

                                        } catch (SQLException e) {
                                            System.out.println("Failed to execute query");
                                            e.printStackTrace();
                                        }

                                        if (areFriends) {
                                            System.out.println("Login user and friend are already friends.");
                                        } else {
                                            
                                            String addFriendshipQuery = "INSERT INTO Friendship (user_id, friend) VALUES (?, ?)";
                                            try (PreparedStatement addFriendshipStatement = connection.prepareStatement(addFriendshipQuery)) {
                                                addFriendshipStatement.setString(1, login_id);
                                                addFriendshipStatement.setString(2, friend_id);

                                                int rowsAffected = addFriendshipStatement.executeUpdate();

                                                if (rowsAffected > 0) {
                                                    System.out.println("Friendship created successfully!");
                                                } else {
                                                    System.out.println("Failed to create friendship. Please try again.");
                                                }

                                            } catch (SQLException e) {
                                                System.out.println("Failed to execute query");
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
//=================================================================================================================================================================================
                                if (choiceinput.equals("4")) {
                                    System.out.println("============================================");
                                    System.out.print("~~~Review Business~~~\n");

                                    
                                    System.out.print("Enter the Business ID you want to review: ");
                                    String businessIdToReview = scanner.nextLine().trim();

                                    
                                    System.out.print("Please Enter The Number of Stars You Would Like To Give This Business: ");
                                    int stars = scanner.nextInt();
                                    scanner.nextLine(); 

                                  
                                    String checkReviewQuery = "SELECT * FROM Review WHERE user_id = ? AND business_id = ?";
                                    boolean hasPreviousReview = false;

                                    try (PreparedStatement checkReviewStatement = connection.prepareStatement(checkReviewQuery)) {
                                        checkReviewStatement.setString(1, login_id);
                                        checkReviewStatement.setString(2, businessIdToReview);

                                        ResultSet checkReviewResultSet = checkReviewStatement.executeQuery();
                                        hasPreviousReview = checkReviewResultSet.next();

                                    } catch (SQLException e) {
                                        System.out.println("Failed to execute query");
                                        e.printStackTrace();
                                    }

                                    if (hasPreviousReview) {
                                        
                                        String updateReviewQuery = "UPDATE Review SET stars = ? WHERE user_id = ? AND business_id = ?";
                                        try (PreparedStatement updateReviewStatement = connection.prepareStatement(updateReviewQuery)) {
                                            updateReviewStatement.setInt(1, stars);
                                            updateReviewStatement.setString(2, login_id);
                                            updateReviewStatement.setString(3, businessIdToReview);

                                            int rowsAffected = updateReviewStatement.executeUpdate();

                                            if (rowsAffected > 0) {
                                                System.out.println("Review updated successfully!");
                                            } else {
                                                System.out.println("Failed to update review.");
                                            }

                                        } catch (SQLException e) {
                                            System.out.println("Failed to execute query");
                                            e.printStackTrace();
                                        }
                                    } else {
                                        
                                        String insertReviewQuery = "INSERT INTO Review (user_id, business_id, stars, date) VALUES (?, ?, ?, GETDATE())";
                                        try (PreparedStatement insertReviewStatement = connection.prepareStatement(insertReviewQuery)) {
                                            insertReviewStatement.setString(1, login_id);
                                            insertReviewStatement.setString(2, businessIdToReview);
                                            insertReviewStatement.setInt(3, stars);

                                            int rowsAffected = insertReviewStatement.executeUpdate();

                                            if (rowsAffected > 0) {
                                                System.out.println("Review recorded successfully!");
                                            } else {
                                                System.out.println("Failed to record review.");
                                            }

                                        } catch (SQLException e) {
                                            System.out.println("Failed to execute query");
                                            e.printStackTrace();
                                        }
                                    }
                                }



//=================================================================================================================================================================================

                                if (choiceinput.equals("5")) {
                                    System.out.print("exiting the Program now...");
                                    System.out.print("\nThank you for using the yelp database");
                                    System.out.print("\ngoodbye");
                                    break;

                                } else {
                                    System.out.print("\nPlease Select a Valid Option \n");

                                }
                            }
                        }
                        //-------------------------------------------------------------------------------------
                        else {
                            System.out.println("Invalid user ID. Please try again.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Failed to execute query");
                        e.printStackTrace();
                    }
                }
            }} catch (SQLException e) {
            System.out.println("Failed to connect");
            e.printStackTrace();
        } finally {
            scanner.close(); 
        }
    }
}
