---------------------- MODULE bool ----------------------
EXTENDS Integers, FiniteSets

CONSTANTS first, second


VARIABLES flag_locked, who_is_in, who_is_out, who_is_in_while, who_is_after_while


vars == <<flag_locked, who_is_in, who_is_out, who_is_in_while, who_is_after_while>> 

(***************************************************************************)
TypeOK == (Cardinality(who_is_in) <= 1) 

Init == /\ who_is_in = { }
        /\ flag_locked = "F"
        /\ who_is_in_while = {first, second}
        /\ who_is_out = {}
        /\ who_is_after_while = {}
              
                
GoInWhile(person) == IF flag_locked = "F" THEN
                       /\ who_is_after_while' = who_is_after_while \cup {person}
                       /\ who_is_in_while' = who_is_in_while \ {person}
                      /\ who_is_in' = who_is_in
                      /\ who_is_out' = who_is_out
                      /\ flag_locked' = flag_locked
                    ELSE
                            flag_locked' = flag_locked
                            /\ who_is_after_while' = who_is_after_while
                             /\ who_is_in_while' = who_is_in_while
                            /\ who_is_in' = who_is_in
                              /\ who_is_out' = who_is_out
                              
                              
GoAfterWhile1(person) == IF person \in who_is_after_while THEN
                             /\ flag_locked' = "T"
                            /\ who_is_after_while' = who_is_after_while
                             /\ who_is_in_while' = who_is_in_while
                            /\ who_is_in' = who_is_in
                              /\ who_is_out' = who_is_out
                         ELSE
                             flag_locked' = flag_locked
                            /\ who_is_after_while' = who_is_after_while
                             /\ who_is_in_while' = who_is_in_while
                            /\ who_is_in' = who_is_in
                              /\ who_is_out' = who_is_out

GoAfterWhile2(person) == IF person \in who_is_after_while /\  flag_locked = "T" THEN
                            who_is_in' = who_is_in \cup {person}
                         /\ who_is_after_while' = who_is_after_while \ {person}
                         /\ flag_locked' = flag_locked
                             /\ who_is_in_while' = who_is_in_while
                              /\ who_is_out' = who_is_out
                         ELSE
                            flag_locked' = flag_locked
                            /\ who_is_after_while' = who_is_after_while
                             /\ who_is_in_while' = who_is_in_while
                            /\ who_is_in' = who_is_in
                              /\ who_is_out' = who_is_out

GoAfterWhile3(person) == IF person \in who_is_in THEN
                            who_is_in' = who_is_in \ {person}
                            /\ flag_locked' = "F"
                            /\ who_is_after_while' = who_is_after_while
                             /\ who_is_in_while' = who_is_in_while
                              /\ who_is_out' = who_is_out
                        ELSE
                           flag_locked' = flag_locked
                            /\ who_is_after_while' = who_is_after_while
                             /\ who_is_in_while' = who_is_in_while
                            /\ who_is_in' = who_is_in
                              /\ who_is_out' = who_is_out

GoAfterWhile4(person) == IF person \notin who_is_in_while /\ person \notin who_is_after_while THEN
                            who_is_in_while' = who_is_in \cup {person}
                            /\ flag_locked' = flag_locked
                            /\ who_is_after_while' = who_is_after_while
                            /\ who_is_in' = who_is_in
                              /\ who_is_out' = who_is_out

                          ELSE
                            flag_locked' = flag_locked
                            /\ who_is_after_while' = who_is_after_while
                             /\ who_is_in_while' = who_is_in_while
                            /\ who_is_in' = who_is_in
                              /\ who_is_out' = who_is_out



                

(***************************************************************************)
(* The next-state formula Next describes all steps s -> t that represent a *)
(* safe move of a set S of people across the river starting from           *)
(* bank_of_boat.  It asserts that there exists some subset S of the set of *)
(* people on the bank where the boat is for which step s -> t describes a  *)
(* safe movement of the people in S to the other bank.  This assertion is  *)
(* expressed mathematically with the existential quantification operator   *)
(* \E (written by mathematicians as an upside down E), where               *)
(*                                                                         *)
(*    \E x \in T : A(x)                                                    *)
(*                                                                         *)
(* asserts that A(x) is true for at least one value x in the set T.        *)
(***************************************************************************)
Next == \E person \in {first, second} :
             GoInWhile(person)
             \/ GoAfterWhile1(person)
             \/ GoAfterWhile2(person)
             \/ GoAfterWhile3(person)
             \/ GoAfterWhile4(person)
            
\*Spec == Init /\ [][Next]_vars 

(***************************************************************************)
(* The usual reason for writing a spec is to check the system you're       *)
(* specifying for errors.  This means checking that all possible           *)
(* executions satisfy some property.  The most commonly checked property   *)
(* is invariance, asserting that some condition is satisfied by every      *)
(* state in in every possible execution.                                   *)
(*                                                                         *)
(* The purpose of this spec is to solve the cannibals and missionaries     *)
(* problem, which means finding some possible execution in which everyone  *)
(* reaches bank "W".  We can find that solution by having the TLC model    *)
(* checker check the invariance property that, in every reachable state,   *)
(* there is someone left on bank "E".  When TLC find that an invariant     *)
(* it's checking isn't an invariant, it outputs an execution that reaches  *)
(* a state in which the invariant isn't true--which in this case means an  *)
(* execution that solves the problem (one ending in a state with no one on *)
(* bank "E").  So to find the solution, you just have to run TLC on a      *)
(* model of this specification in which three-element sets are substituted *)
(* for the constants Missionaries and Cannibals, instructing TLC to check  *)
(* that the formula                                                        *)
(*                                                                         *)
(*    who_is_on_bank["E"] /= {}                                            *)
(*                                                                         *)
(* is an invariant.  The error trace TLC produces is a solution to the     *)
(* problem.  You can run TLC from the TLA+ Toolbox.  Go to the TLA+ web    *)
(* page to find out how to learn to do that.                               *)
(*                                                                         *)
(* This problem was proposed to me by Jay Misra, who then suggested        *)
(* improvements to my first version of the spec.                           *)
(***************************************************************************)                  
=============================================================================
\* Modification History
\* Last modified Sat Jan 09 23:48:54 MSK 2021 by mi
\* Last modified Sat Dec 22 14:17:18 PST 2018 by lamport
\* Created Thu Dec 20 11:44:08 PST 2018 by lamport
