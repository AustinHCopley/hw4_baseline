package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Model for the application that tracks expenses
 * Applies the Oberver design pattern
 * Observes changes to expenses and notifies the listeners when these changes occur
 */
public class ExpenseTrackerModel {

  //encapsulation - data integrity
  private List<Transaction> transactions;
  private List<Integer> matchedFilterIndices;
  private List<ExpenseTrackerModelListener> listenerList;

  // This is applying the Observer design pattern.                          
  // Specifically, this is the Observable class. 
    
  public ExpenseTrackerModel() {
    transactions = new ArrayList<Transaction>();
    matchedFilterIndices = new ArrayList<Integer>();
    listenerList = new ArrayList<ExpenseTrackerModelListener>();
  }

  public void addTransaction(Transaction t) {
    // Perform input validation to guarantee that all transactions added are non-null.
    if (t == null) {
      throw new IllegalArgumentException("The new transaction must be non-null.");
    }
    transactions.add(t);
    // The previous filter is no longer valid.
    matchedFilterIndices.clear();
    stateChanged(); // handle updates with protected method here
  }

  public void removeTransaction(Transaction t) {
    transactions.remove(t);
    // The previous filter is no longer valid.
    matchedFilterIndices.clear();
    stateChanged();
  }

  public List<Transaction> getTransactions() {
    //encapsulation - data integrity
    return Collections.unmodifiableList(new ArrayList<>(transactions));
  }

  public void setMatchedFilterIndices(List<Integer> newMatchedFilterIndices) {
      // Perform input validation
      if (newMatchedFilterIndices == null) {
	  throw new IllegalArgumentException("The matched filter indices list must be non-null.");
      }
      for (Integer matchedFilterIndex : newMatchedFilterIndices) {
	  if ((matchedFilterIndex < 0) || (matchedFilterIndex > this.transactions.size() - 1)) {
	      throw new IllegalArgumentException("Each matched filter index must be between 0 (inclusive) and the number of transactions (exclusive).");
	  }
      }
      // For encapsulation, copy in the input list 
      this.matchedFilterIndices.clear();
      this.matchedFilterIndices.addAll(newMatchedFilterIndices);
      stateChanged();
  }

  public List<Integer> getMatchedFilterIndices() {
      // For encapsulation, copy out the output list
      List<Integer> copyOfMatchedFilterIndices = new ArrayList<Integer>();
      copyOfMatchedFilterIndices.addAll(this.matchedFilterIndices);
      return copyOfMatchedFilterIndices;
  }

  /**
   * Registers the given ExpenseTrackerModelListener for
   * state change events.
   *
   * @param listener The ExpenseTrackerModelListener to be registered
   * @return If the listener is non-null and not already registered,
   *         returns true. If not, returns false.
   */   
  public boolean register(ExpenseTrackerModelListener listener) {
      // For the Observable class, this is one of the methods.
      //
      if (listener == null) {
        return false;
      }
      if (containsListener(listener)) {
        return false;
      }
      this.listenerList.add(listener);
      return true;
  }

  /**
   * Gets the number of registered listeners.
   *
   * @return int: number of registered listeners
   */
  public int numberOfListeners() {
      // For testing, this is one of the methods.
      //
      return this.listenerList.size();
  }

  /**
   * Checks if a listener is registered.
   *
   * @param listener The ExpenseTrackerModelListener to check
   * @return boolean: True if listener is registered, false if not
   */
  public boolean containsListener(ExpenseTrackerModelListener listener) {
      // For testing, this is one of the methods.
      //
      if (listener == null) {
        return false;
      }
      return this.listenerList.contains(listener);
  }

  /**
   * Notifies listener about state changes.
   */
  protected void stateChanged() {
      // For the Observable class, this is one of the methods.
      //
      for (ExpenseTrackerModelListener listener : listenerList) {
        listener.update(this);
      }
  }
}
