import pandas as pd
from matplotlib import pyplot as plt
import numpy as np
import seaborn as sns
import sys

plt.rcParams["figure.figsize"] = [14, 8]
plt.rcParams['font.size'] = '15'
plt.rcParams['lines.linewidth'] = '2'
plt.rcParams['lines.markersize'] = '10'
plt.rcParams["figure.autolayout"] = True

columns = ["size", "time"]
df = pd.read_csv('semestr3\Wdpr\zadania\projekty\output\Mandel_times.csv', header=None, names=columns)

df["size"] = df["size"]**2
sns.set_style("whitegrid")

fig, ax = plt.subplots()

#g1 = sns.lineplot(x = 'size', y= 'time', data=df, errorbar='sd', ax=ax, markers=True, err_style='bars', err_kws={'elinewidth':1}, marker="o")
g1 = sns.regplot(x = 'size', y= 'time', data=df, ax=ax, marker="o", order=2, x_estimator=np.mean)
g1.set_xlabel('size [px]')
g1.set_ylabel('time [ms]')

plt.show()