---------------------- MODULE spinlock ----------------------
EXTENDS Integers, FiniteSets

VARIABLES flag_locked, who_is_in_critical, count
           
Init == /\ who_is_in_critical = {}
        /\ flag_locked = "F"
        /\ count = 100
        
GoOut(person) ==  IF person \in who_is_in_critical THEN
                    who_is_in_critical' =(who_is_in_critical \ {person})
                    /\ flag_locked' = "F"
                    /\ count' = count
                ELSE
                    flag_locked' = flag_locked
                    /\ who_is_in_critical' = who_is_in_critical
                    /\ count' = count
                
                
GoIn(person) == IF person \notin who_is_in_critical /\ flag_locked = "F" THEN
                    who_is_in_critical' = (who_is_in_critical \cup {person})
                    /\ flag_locked' = "T"
                    /\ count' = count
                ELSE
                    flag_locked' = flag_locked
                    /\ who_is_in_critical' = who_is_in_critical
                    /\ count' = count

Next == \E person \in 1..count:
             \/ GoIn(person)
             \/ GoOut(person)
             
 =============================================================================