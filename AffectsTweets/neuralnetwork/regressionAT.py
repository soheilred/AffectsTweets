import numpy as np
from sklearn.svm import SVR
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
from sklearn.model_selection import cross_val_score
# from sklearn import datasets

# ########################################
# Reading feature vectors
features = np.matrix([0.0 , 0.0 , 0.0])
output = np. matrix([0.0])
input = file('RankLib', 'r')
lines = input.readlines()
input.close()
# tweetNum = []
for line in lines:
	variablesArray = line.split('\t')
	# tweetNum.append(variablesArray[0])
	floatsArray = map(float , variablesArray)
	features = np.append(features, [[ floatsArray[2] , floatsArray[3] , floatsArray[4] ]], axis = 0)
	output = np.append(output, [[floatsArray[1]]], axis = 0)


# selector = SelectPercentile(f_classif, 50)
# selector.fit(X_train, y_train)
# X_train_transformed = selector.transform(X_train)
# X_test_transformed = selector.transform(X_test)
# #############################################################################
# Define feature and output for the SVR
X = features #np.sort(5 * np.random.rand(40, 1), axis=0)
y = np.squeeze(np.asarray(output)) #np.sin(X).ravel() np.transpose(

# Use sklearn to select 60% features
X_train, X_test, y_train, y_test = train_test_split(
	X, y, test_size=0.6, random_state=0)
X_valid, X_test, y_valid, y_test = train_test_split(X_train, y_train, test_size=0.5, random_state=None)
# print "Training set shape"
# print X_train.shape, y_train.shape
# print "Test set shape"
# print X_test.shape, y_test.shape
# #############################################################################
# Add noise to targets
# y[::5] += 3 * (0.5 - np.random.rand(8))

# #############################################################################
# Fit regression model
svr_rbf = SVR(kernel='rbf', C=1e5, gamma=0.1, epsilon= 0.1)
# svr_lin = SVR(kernel='linear', C=1e3)
# svr_poly = SVR(kernel='poly', C=1e3, degree=2)
y_rbf = svr_rbf.fit(X_train, y_train) #.fit(X_train, y_train) .predict(X)
# y_lin = svr_lin.fit(X_train, y_train)#.predict(X)
# y_poly = svr_poly.fit(X_train, y_train)#.predict(X)
# scores = cross_val_score(y_rbf, X, y, cv=5)
# y_rbf.score(X_test, y_test) 10**i
valid_prediction = y_rbf.predict(X_valid)
error = np.sum((valid_prediction - y_valid)**2)
print error

y_predition_test = y_rbf.predict(X_test)
print y_predition_test.shape
# Finding C for our SVR
# errorC = np.matrix([0,0])
# print error
# for i in range(150000, 1500000, 100000):
# 	svr_rbf = SVR(kernel='rbf', C=i, gamma=0.1, epsilon= 0.1)
# 	y_rbf = svr_rbf.fit(X_train, y_train) #.fit(X_train, y_train) .predict(X)
# 	valid_prediction = y_rbf.predict(X_valid)
# 	error = np.sum((valid_prediction - y_valid)**2)
# 	errorC = np.append(errorC, [[error,i ]], axis=0)

# print errorC
# #############################################################################
# Look at the results
# lw = 2
# plt.scatter(X, y, color='darkorange', label='data')
# plt.plot(X, y_rbf, color='navy', lw=lw, label='RBF model')
# plt.plot(X, y_lin, color='c', lw=lw, label='Linear model')
# plt.plot(X, y_poly, color='cornflowerblue', lw=lw, label='Polynomial model')
# plt.xlabel('data')
# plt.ylabel('target')
# plt.title('Support Vector Regression')
# plt.legend()
# plt.show()

# #################################################
# Write to a file
svm_file = file('svm.results', 'w+')
for i in range(323):
    svm_file.write(str(31294 + i) + '\tabc\tjoy\t' + str(y_predition_test.item(i)) + '\n' )

svm_file.close()

datasetFile = file('joy_train', 'w+')
for i in range(323):
    datasetFile.write(str(31294 + i) + '\tabc\tjoy\t' + str(y.item(1294 + i)) + '\n' )

datasetFile.close()