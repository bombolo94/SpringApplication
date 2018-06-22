package com.spring.container;

import com.spring.context.Context;
import com.spring.specification.Specification;
import com.spring.values.Values;

public class UrbanDataset
{
  private Specification specification;
  private Context context;
  private Values values;
  
  public UrbanDataset() {}
  
  public Specification getSpecification()
  {
    return specification;
  }
  
  public void setSpecification(Specification specification) { this.specification = specification; }
  
  public Context getContext()
  {
    return context;
  }
  
  public void setContext(Context context) { this.context = context; }
  
  public Values getValues() {
    return values;
  }
  
  public void setValues(Values values) { this.values = values; }
}