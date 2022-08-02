package com.oro.orderextension;

public abstract interface WizardPage
{
  public abstract String getName();
  
  public abstract boolean canGoNext();
  
  public abstract boolean canGoBack();
  
  public abstract boolean canFinishWizard();
  
  public abstract boolean finish();
  
  public abstract WizardPage getPreviousPage();
  
  public abstract WizardPage getNextPage();
}
