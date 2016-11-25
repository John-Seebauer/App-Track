from math import sqrt 
from dataset import dataset

def get_similarity(dataset, p1, p2):
  p1D = dataset[p1]
  p2D = dataset[p2]
  
  r1Sum=0
  r2Sum=0 
  r1SqrSum=0
  r2SqrSum=0 
  productSum=0 
  same_rating=[]
  for rating1 in p1D:
    for rating2 in p2D:
      if rating1[0] == rating2[0]:
	same_rating.append((rating1,rating2))
	r1Sum+=rating1[1]
	r1SqrSum+=pow(rating1[1],2)
	r2Sum+=rating2[1]
	r2SqrSum+=pow(rating2[1],2)
	productSum+=rating1[1]*rating2[1]

  N = len(same_rating)
  if 0 == N:
    print "no similarities"
    return 0
 
  Sxx = r1SqrSum - pow(r1Sum,2)/N
  Syy = r2SqrSum - pow(r2Sum,2)/N
  Sxy = productSum - r1Sum*r2Sum/N

  if Sxx*Syy == 0: 
    #print "denomo zero "+str(N)
    #print str(Sxx) + " : " + str(r1SqrSum) +","+str(r1Sum)
    return 0
  return Sxy/(sqrt(Sxx*Syy))


def get_all_similarities(dataset, p1):
  scores = [ (other, get_similarity(dataset, p1, other)) for other in dataset if other != p1]
  scores.sort(key = lambda a:a[1])
  scores.reverse();
  return scores

def get_recomendations(dataset, p1):
  if p1 not in dataset:
    return []

  simPpl = get_all_similarities(dataset, p1)
  seenMovies = { movie[0] for movie in dataset[p1]}
  recomendations = {} #will have (movie, sum of recs, total # of recs recieved
  for simUser in simPpl:
    if simUser[0] == p1:
      continue
    
    if simUser[1] ==0: #no corralation, waste of time - can broaden this for performance
      continue

    user = simUser[0]
    sim = simUser[1]

    #need to adjust for users optimism
    for rec in dataset[user]:
      if rec[0] in seenMovies: #dont want to recomend old movies
	continue
      
      if rec[0] in recomendations:
	recomendations[rec[0]][0]+=rec[1]*sim #need a modifier on this for optimism
	recomendations[rec[0]][1]+=1
      else:
	recomendations[rec[0]] = [rec[1]*sim, 1]

    weightedRecs = [ (movie, recomendations[movie][0]/recomendations[movie][1]) for movie in recomendations ]

    weightedRecs.sort(key = lambda a:a[1])
    return weightedRecs


   
  
print get_recomendations(dataset, 'John');
