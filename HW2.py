# Program that reads a review and determines whether it's positive or negative.
# Edward Zhao
# PUID: 0030621301

import re
from collections import Counter


def readAndDivideBySentiment(fileName):
    try:
        file1 = open(fileName, "r")
        list_negative = []
        list_positive = []
        for i in file1.readlines():
            if i[len(i) - 2] == "0":  # if negative review
                i = i[:-3]  # removes the space and 0 at the end
                list_negative.append(i)
            elif i[len(i) - 2] == "1":  # if positive review
                i = i[:-3]  # removes the space and 1 at the end
                list_positive.append(i)
    except IOError:
        print 'File not found!'
        return
    return list_positive, list_negative


def cleanData(myData):
    for i in range(0, len(myData)):
        myData[i] = myData[i].lower()
        myData[i] = re.sub('[^A-Za-z0-9\'-]+', ' ', myData[i])  # removes special characters besides hyphen/apostrophe
        myData[i] = re.sub('--', ' ', myData[i])  # removes double hyphen and replacing with space
        myData[i] = re.sub('[-]', '', myData[i])  # removes hyphen
        words = myData[i].split(' ')
        final_string = ''
        for j in words:
            new_str = ''
            matched = False
            match = re.match(r"([a-z']+)([0-9]+)([a-z']+)", j, re.I)
            if not match:  # do nothing for numbers in between letters
                match = re.match(r"([0-9]+)([a-z']+)([0-9]+)", j, re.I)
                if match:
                    new_str += 'num ' + match.groups()[1] + ' num'
                    matched = True
                else:
                    match = re.match(r"([0-9]+)([a-z']+)", j, re.I)  # match/replace numbers with 'num' on only 1 side
                    if match:
                        new_str += 'num ' + match.groups()[1]
                        matched = True
                    else:
                        match = re.match(r"([a-z']+)([0-9]+)", j, re.I)
                        if match:
                            new_str += match.groups()[0] + ' num'
                            matched = True
            else:
                new_str += match.groups()[0] + match.groups()[1] + match.groups()[2]
                matched = True
            if matched:
                final_string += new_str + ' '
            else:
                final_string += j + ' '
        myData[i] = final_string.strip()
    for i in range(0, len(myData)):
        myData[i] = re.sub(r'\b\d+\b', 'num', myData[i])  # replaces lone numbers with 'num'
    for i in range(0, len(myData)):  # replaces consecutive numbers with 'num'
        words = myData[i].split(' ')
        previous_num = False
        new_str = ''
        for word in words:
            if word == '':
                continue
            if word == 'num':
                if not previous_num:
                    new_str += 'num '
                    previous_num = True
            else:
                previous_num = False
                new_str += word + ' '
        myData[i] = new_str
    return myData


def calculateUniqueWordsFreq(trainData, cutOff):
    if cutOff < 0:
        print 'Cutoff must be at least zero!'
        return
    d = []
    for i in range(len(trainData)):
        word = trainData[i].split(' ')
        d += word
    word_counts = Counter(d)  # counter object that returns how many times a word appears
    final_list = word_counts.most_common()  # puts list in order with most occurring words at the beginning
    d = final_list[cutOff:]  # removes most common numbers according to cutOff value
    dictionary = dict(d)
    return dictionary


def calculateClassProbability(posTrain, negTrain):
    return float(len(posTrain))/float(len(posTrain) + len(negTrain)), \
           float(len(negTrain)/float(len(posTrain) + len(negTrain)))


def calculateScores(classProb, uniqueVocab, testData):
    list_scores = []
    values = uniqueVocab.values()
    denominator = len(uniqueVocab) + sum(values)  # denominator of the formula
    for i in testData:
        word_score_list = []
        arr = i.split(' ')
        for j in arr:
            if j in uniqueVocab:
                word_frequency = uniqueVocab[j]
            else:
                word_frequency = 0
            word_score_list.append(float(word_frequency + 1) / denominator)  # adds each score to list
        score = classProb
        for k in word_score_list:  # multiplies the scores together as stated by the given formula
            score *= k
        list_scores.append(score)
    return list_scores


def calculateAccuracy(positiveTestDataPositiveScores, positiveTestDataNegativeScores, negativeTestDataPositiveScores,
                      negativeTestDataNegativeScores):
    tp = 0
    tn = 0
    fp = 0
    fn = 0
    for i in range(len(positiveTestDataNegativeScores)):
        if positiveTestDataPositiveScores[i] >= positiveTestDataNegativeScores[i]:
            tp += 1
        else:
            fp += 1
    for j in range(len(negativeTestDataNegativeScores)):
        if negativeTestDataNegativeScores[j] > negativeTestDataPositiveScores[j]:
            tn += 1
        else:
            fn += 1
    return tp, fp, tn, fn


def demo(review):
    tup = readAndDivideBySentiment("TRAINING.txt")
    positive_train_unique = calculateUniqueWordsFreq(cleanData(tup[0]), 1)
    negative_train_unique = calculateUniqueWordsFreq(cleanData(tup[1]), 1)
    class_prob = calculateClassProbability(tup[0], tup[1])
    review_list = cleanData([review])
    positive_scores = calculateScores(class_prob[0], positive_train_unique, review_list)
    negative_scores = calculateScores(class_prob[1], negative_train_unique, review_list)
    if positive_scores[0] > negative_scores[0]:
        return 1
    else:
        return -1


def main():
    tup = readAndDivideBySentiment("TRAINING.txt")
    tup_test = readAndDivideBySentiment("TESTING.txt")
    cleanData(tup[0])
    cleanData(tup[1])
    cleanData(tup_test[0])
    cleanData(tup_test[1])
    review = ' '
    while len(review) > 0:
        review = raw_input('Enter sample review: \n')
        if demo(review) == 1:
            print 'Positive'
        elif demo(review) == -1:
            print 'Negative'
    pos_train_unique = calculateUniqueWordsFreq(tup[0], 1)
    neg_train_unique = calculateUniqueWordsFreq(tup[1], 1)
    print neg_train_unique
    class_prob = calculateClassProbability(tup[0], tup[1])
    pospos = calculateScores(class_prob[0], pos_train_unique, tup_test[0])
    posneg = calculateScores(class_prob[1], neg_train_unique, tup_test[0])
    negneg = calculateScores(class_prob[1], neg_train_unique, tup_test[1])
    negpos = calculateScores(class_prob[0], pos_train_unique, tup_test[1])
    print pospos
    print posneg
    print negneg
    print negpos

    print calculateAccuracy(pospos, posneg, negpos, negneg)


main()
