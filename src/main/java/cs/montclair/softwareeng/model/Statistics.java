package cs.montclair.softwareeng.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics {
   private Map<String, Counter> statusCounts = new HashMap<String, Counter>();
   private List<Bug> bugs;

   public Statistics(List<Bug> bugs) {
      this.bugs = bugs;

      countStatuses();
   }

   private void countStatuses() {
      for(Bug bug : bugs) {
         Counter counter = statusCounts.get(bug.getStatus());

         if(counter == null) {
            counter = new Counter();
            statusCounts.put(bug.getStatus(), counter);
         }

         counter.increment();
      }
   }

   public Map<String, Counter> getStatusCounts() {
      return Collections.unmodifiableMap(statusCounts);
   }
}
