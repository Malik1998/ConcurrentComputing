---------------------- MODULE spinlock ----------------------
EXTENDS Integers, FiniteSets

CONSTANTS first, second, no_one


VARIABLES flag_locked, who_is_in, who_is_out

(***************************************************************************)
TypeOK == \/ (who_is_in \subseteq { first } /\ flag_locked \subseteq {"T"}) 
          \/ (who_is_in \subseteq { second } /\ flag_locked \subseteq {"T"})
          \/ (who_is_in \subseteq { no_one } /\ flag_locked \subseteq {"F"})
                          
Init == /\ who_is_in = { no_one }
        /\ flag_locked = {"F"}
        /\ who_is_out = {first, second}
              
(***************************************************************************)
(* We now define some operators that will be used to define the next-state *)
(* formula Next.                                                           *)
(*                                                                         *)
(* We first define IsSafe(S) to be the condition for it to be safe for S   *)
(* to be the set of people on a bank of the river.  It is true iff there   *)
(* are either no missionaries in S or the cannibals in do not outnumber    *)
(* the missionaries in S.  The operator \subseteq is the subset relation,  *)
(* and \cap is the set intersection operator.                              *)
(***************************************************************************)

GoOut(person) == /\ who_is_in \subseteq {person}
                /\  who_is_in' =(who_is_in \ {person}) \cup {no_one}
                /\ who_is_out' = who_is_out \cup {person}
                /\ flag_locked' = {"F"}
                
GoIn(person) == /\ flag_locked \subseteq {"F"}
                /\  who_is_in' = (who_is_in \cup {person}) \ {no_one}
                /\  who_is_out' = who_is_out \ {person}
                /\ flag_locked' = {"T"}

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
             GoIn(person)
             \/ GoOut(person)
            
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
\* Last modified Sat Jan 09 14:36:32 MSK 2021 by mi
\* Last modified Sat Dec 22 14:17:18 PST 2018 by lamport
\* Created Thu Dec 20 11:44:08 PST 2018 by lamport
