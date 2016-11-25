#!/usr/bin/env python
 
# Collabrative Filtering data set taken from Collective Intelligence book.

dataset = {
    'Erik': [('inception',5), 
      ('m1',2),
      ('mean girls',4)],
    'Erick': [('inception',4), 
      ('m1',1),
      ('mean girls',3)],
    'Eric': [('inception',1), 
      ('m1',5),
      ('mean girls',3)],
    'Bob':[('mean girls',4)],
    'Claire':[('princess bride',5),
      ('m2',4),
      ('inception',1)],
    'John': [('inception',3), 
      ('m3',3),
      ('princess bride',4)],
    'Wyatt':[('princess bride',5),
      ('m2',4),
      ('mean girls',3)]
    }
 
dDataset={
 'John': {
  'princess bride': 4,
  'inception':3,
  },
 'Claire': {
  'princess bride': 5,
  'inception':1,
  },
  
 'Bob': {
 'mean girls':4
 },
 'Erik': {
 'inception':5,
 'm1':2,
 'mean girls':4
 },
 'Eric': {
 'inception':1,
 'm1':5,
 'mean girls':3
 },
 'Lisa Rose': {
 'Lady in the Water': 2.5,
 'Snakes on a Plane': 3.5,
 'Just My Luck': 3.0,
 'Superman Returns': 3.5,
 'You, Me and Dupree': 2.5,
 'The Night Listener': 3.0},
 'Gene Seymour': {'Lady in the Water': 3.0,
 'Snakes on a Plane': 3.5,
 'Just My Luck': 1.5,
 'Superman Returns': 5.0,
 'The Night Listener': 3.0,
 'You, Me and Dupree': 3.5},
 
 'Michael Phillips': {'Lady in the Water': 2.5,
 'Snakes on a Plane': 3.0,
 'Superman Returns': 3.5,
 'The Night Listener': 4.0},
 'Claudia Puig': {'Snakes on a Plane': 3.5,
 'Just My Luck': 3.0,
 'The Night Listener': 4.5,
 'Superman Returns': 4.0,
 'You, Me and Dupree': 2.5},
 
 'Mick LaSalle': {'Lady in the Water': 3.0,
 'Snakes on a Plane': 4.0,
 'Just My Luck': 2.0,
 'Superman Returns': 3.0,
 'The Night Listener': 3.0,
 'You, Me and Dupree': 2.0},
 
 'Jack Matthews': {'Lady in the Water': 3.0,
 'Snakes on a Plane': 4.0,
 'The Night Listener': 3.0,
 'Superman Returns': 5.0,
 'You, Me and Dupree': 3.5},
 
 'Toby': {'Snakes on a Plane':4.5,
 'You, Me and Dupree':1.0,
 'Superman Returns':4.0}}
