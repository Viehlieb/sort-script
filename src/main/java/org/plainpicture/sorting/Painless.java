package org.plainpicture.sorting;

public class Painless {
  double checkDouble(def val){
        return (val != null) ? (double) val : 0.0d;  
  }  
  long checkLong(def val)
  {    
    return (val != null) ? (long) val : -1l;  
  } 
  double ageScore(def batched_at, def now) 
  {  
    def value = checkLong(batched_at.toEpochSecond());
     if(value == -1)
      return 0.0;
      return Math.pow(1.0 - Math.pow(((double)now - Math.min((double)now, (double)value)) / ((double)now - 978307200000.0), 0.75), 2.0);
      }
  long getKeywordCountryId(String field)
     {    return checkLong(field);  }
  long getCreatorCountryId(String field)
    {    return checkLong(field);  }
  double getRand(def rand) {    return checkDouble(rand);  }
  
  double keywordScore(def field, def keywords)
  {  
    def res = 0.0;
    int n = 0;
    if (keywords.size() == 0)
    {    
      return 0.0;
    }
    if (field == \"\u007c\" || field == null)
    {
      return 0.0;
    } 
    def term_scores_array= field.splitOnToken(\" \");
    for (String s : term_scores_array)  
    {    
      String[] element = s.splitOnToken(\"\u007c\");
      if(keywords.contains(element[0].trim()))
      {      
        res += Double.parseDouble(element[1].trim());    
      }    
      n++;
    }
    if (n== 0)  
    {    
      return 0.0;
    } 
    double nDouble = n;
    return (res /  nDouble);
  }
  long getPrimaryRank(def primary_rank)
  {    
     return  checkLong(primary_rank);  
  }  
  double collectionScore(def collection_score) 
  {    
    return checkDouble(collection_score);  
  }  
  double supplierScore(def supplier_score) 
  {    
    return checkDouble(supplier_score);  
  }  
  double baseScore(def score) 
  {    
    return checkDouble(score);  
  }  
  def now = System.currentTimeMillis();
  def random = new Random();
  long primaryRank = params.ignore_primary_rank ? -1 : getPrimaryRank((doc.primary_rank.size()==0)? -1l : doc.primary_rank);
  String term_scores_field;
  if (params.keyword_field== \"term_scores.de\")
  {
    term_scores_field= params._source.term_scores.de;
  } 
  else if (params.keyword_field==\"term_scores.en\")
  {
    term_scores_field= params._source.term_scores.en;
  }
  else if (params.keyword_field==\"term_scores.fr\")
  {
    term_scores_field= params._source.term_scores.fr;
  }
  else
  { 
    term_scores_field= \"\u007c\";
  }
  def keywords = params.keywords;
  double baseScore = 0.0;
  random.setSeed((int)(getRand(doc.rand.value) * 100000));
  def offset = (params.offset != null)? params.offset : 0;    
  if(primaryRank != -1) 
  {      
    if(primaryRank < offset)
    {
      return offset + (offset - (double)primaryRank);  
    }    
    else        
    {
      baseScore = 1.0 - ((double)primaryRank - offset) / offset;
    }
  } 
  else 
  {      
    baseScore = params.base_weight > 0 ? baseScore((doc.score.size()==0)? 0 : doc.score.value) : 0.0;
  }
  offset + baseScore * (1.0 - params.base_rand * random.nextDouble()) * params.base_weight + ageScore(doc.batched_at.value, now) * (1.0 - params.age_rand * random.nextDouble()) * params.age_weight + keywordScore(term_scores_field, keywords) * (1.0 - params.keyword_rand * random.nextDouble()) * params.keyword_weight  + ((params.collection_weight > 0) ? (collectionScore(doc.collection_score.value) * (1.0 - params.collection_rand * random.nextDouble()) * params.collection_weight) : 0.0)      + ((params.supplier_weight > 0) ? supplierScore(doc.supplier_score.value) * (1.0 - params.supplier_rand * random.nextDouble()) * params.supplier_weight : 0.0d); 
}
