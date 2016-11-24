from math import sqrt 
from collab_filter import get_similarity
from dataset import dataset,dDataset

def pearson_correlation(dataset, person1,person2):
 
  # To get both rated items
  both_rated = {}
  for item in dataset[person1]:
    if item in dataset[person2]:
      both_rated[item] = 1
 
  number_of_ratings = len(both_rated)		
  
  # Checking for number of ratings in common
  if number_of_ratings == 0:
    return 0
 
  # Add up all the preferences of each user
  person1_preferences_sum = sum([dataset[person1][item] for item in both_rated])
  person2_preferences_sum = sum([dataset[person2][item] for item in both_rated])
 
  # Sum up the squares of preferences of each user
  person1_square_preferences_sum = sum([pow(dataset[person1][item],2) for item in both_rated])
  person2_square_preferences_sum = sum([pow(dataset[person2][item],2) for item in both_rated])
 
  # Sum up the product value of both preferences for each item
  product_sum_of_both_users = sum([dataset[person1][item] * dataset[person2][item] for item in both_rated])
 
  # Calculate the pearson score
  numerator_value = product_sum_of_both_users - (person1_preferences_sum*person2_preferences_sum/number_of_ratings)
  denominator_value = sqrt((person1_square_preferences_sum - pow(person1_preferences_sum,2)/number_of_ratings) * (person2_square_preferences_sum -pow(person2_preferences_sum,2)/number_of_ratings))
  if denominator_value == 0:
    return 0
  else:
    r = numerator_value/denominator_value
    return r



def simTest(p1, p2):
  print get_similarity(dataset, p1,p2)
  print pearson_correlation(dDataset, p1,p2)

print "testing"
simTest('John','Claire')

