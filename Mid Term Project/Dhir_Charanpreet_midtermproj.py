import itertools
import sys

def scanningmyitems(Hashed_values, min_Sup, T_items, new_val):
    appearedlist = []
    removed = []
    Total_transaction= len(T_items)
    for val in range(len(Hashed_values)):
        if val%2 !=0:
            support = (Hashed_values[val] / Total_transaction)*100
            if support >= min_Sup:
                appearedlist.append(Hashed_values[val-1])
                appearedlist.append(Hashed_values[val])
            else :
                removed.append(Hashed_values[val-1])
    for i in appearedlist:
        new_val.append(i)
#    print("new_val:", new_val)
#    print("appearedlist:", appearedlist)
#    print("removed",removed)
    if len(appearedlist) == 2 or len(appearedlist) == 0:
        print("This will be returned")
        ret_val = new_val
        return ret_val

    else:
        newsetofvalues(T_items, removed, appearedlist, min_Sup)
    
def newsetofvalues(T_items, removed, appearedlist, min_Sup):
    newfromold = []
    combine = []
    candidate_array = []
    Total_transaction= len(T_items)
    for val in range(len(appearedlist)):
        if val%2 == 0:
            newfromold.append(appearedlist[val])
    for new_item in newfromold:
        tempArray = []
        k = newfromold.index(new_item)
        for val in range(k + 1, len(newfromold)):
            for j in new_item:
                if j not in tempArray:
                    tempArray.append(j)
            for m in newfromold[val]:
                if m not in tempArray:
                    tempArray.append(m)
            combine.append(tempArray)
            tempArray = []
    sortedArray = []
    uniqueArray = []
    for val in combine:
        sortedArray.append(sorted(val))
    for i in sortedArray:
        if i not in uniqueArray:
            uniqueArray.append(i)
    combine = uniqueArray
    for new_item in combine:	
        count = 0
        for transaction in T_items:
            if set(new_item).issubset(set(transaction)):
                count = count + 1
        if count != 0:
            candidate_array.append(new_item)
            candidate_array.append(count)
    scanningmyitems(candidate_array, min_Sup, T_items, new_val)

#   This function takes all the frequent sets as the input and generates Association Rules
def Set_association(new_val):
    Association_Rule = []
    for items in new_val:
        if isinstance(items, list):
            if len(items) != 0:
                term = len(items) - 1
                while term > 0:
                    combinations = list(itertools.combinations(items, term))
                    temporary = []
                    valuesinone = []
                    for otherval in combinations:
                        valuesinone = set(items) - set(otherval)
                        temporary.append(list(valuesinone))
                        temporary.append(list(otherval))
                        Association_Rule.append(temporary)
                        temporary = []
                    term = term - 1
#	print ("here i print",Association_Rule)
    return Association_Rule

#   This function creates the final output of the algorithm by taking Association Rules as the input
def Output(set_for_association, T_items, min_Sup, min_Conf):
    Total_transaction= len(T_items)
    returnOutput = []
    for association in set_for_association:
        Numeratorofsingle = 0
        Percentage_Numeratorofsingle = 0
        Numeratorofdouble = 0
        Percentage_Numeratorofdouble = 0
        for item in T_items:
            if set(association[0]).issubset(set(item)):
                Numeratorofsingle = Numeratorofsingle + 1
            if set(association[0] + association[1]).issubset(set(item)):
                Numeratorofdouble = Numeratorofdouble + 1
        Percentage_Numeratorofsingle = (Numeratorofsingle  / Total_transaction) * 100
        Percentage_Numeratorofdouble = (Numeratorofdouble / Total_transaction) * 100
        conf = (Percentage_Numeratorofdouble / Percentage_Numeratorofsingle) * 100
        if conf >= min_Conf:
            Numeratorofsingle_join = "Support " + str(round(Percentage_Numeratorofsingle, 2))
            Numeratorofdouble_join = "Support " + str(round(Percentage_Numeratorofdouble))
            Confidence_join = "Confidence " + str(round(conf))

            returnOutput.append(Numeratorofsingle_join)
            returnOutput.append(Numeratorofdouble_join)
            returnOutput.append(Confidence_join)
            returnOutput.append(association)

    return returnOutput
    
def printreport(AprioriOutput):
    count = 1
    if len(AprioriOutput) == 0:
        print("For given value of Confidence and support no Association_Rule")
    else:
        for val in AprioriOutput:
            if count == 4:
                print(str(val[0]) + "=>" + str(val[1]))
                count = 0
            else:
                print(val, end='  ')
            count = count + 1	
Database=sys.argv[1];

with open(Database,'r') as DB:  #fileobject to open file
    values = DB.readlines()   #readlines method to read line in file until end of file

print("\n \t \t \t \t APRIORI ALGORITHM  \n")
min_Sup = int(input('Minimum Support taken for Algorithm :  '))
min_Conf = int(input('Minimum Confidence taken for Algorithm : '))

T_items = [];

for item in values:  #for every item in the database    
    item = item.strip() #removes characters from starting and the end
    T_items.append(item.split(",")) #stores item by removing the separations character (,)

#count_transac= len(open(Database).readlines(  ))
Hash_Item = {}
Hashed_values = []
for items in T_items:
    for Hash_key in items:
        if Hash_key not in Hash_Item:
            Hash_Item[Hash_key] = 1
        else:
            Hash_Item[Hash_key] = Hash_Item[Hash_key] + 1
for key in Hash_Item:
    Store_val = []
    Store_val.append(key)
    Hashed_values.append(Store_val)
    Hashed_values.append(Hash_Item[key])
    Store_val = []
print (" \n Items and Total number of items in my data set : \n ")
print (Hash_Item)
print (" \n")
new_val=[]
num_items=float(len(T_items))  
frequentItemSet = scanningmyitems(Hashed_values, min_Sup, T_items, new_val)
set_for_association = Set_association(new_val)
AprioriOutput = Output(set_for_association, T_items, min_Sup, min_Conf)
print_report=printreport(AprioriOutput)
