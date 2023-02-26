// export function tokenize(str) {
// 	let nameTokens = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_$0123456789",
// 		ret = [],
// 		j = 0, i, c, t
// 	for (; j < str.length;) {
// 		c = str[i = ++j]
// 		if (nameTokens.includes(c)) {
// 			t = c
// 			while (nameTokens.includes(str[++j])) t += str[j]
// 			ret.push({ s: t, i })
// 			j--
// 		} else if ("'\"`".includes(c)) {
// 			t = c
// 			while (str[++j] != c) t += str[j] == "\\" ? str[j++] + str[j] : str[j]
// 			ret.push({ s: t + str[j], i })
// 		} else if ("=&|*".includes(c))
// 			ret.push({ s: str[j + 1] == c ? c + str[++j] : c, i })
// 		else if (" \n\t".includes(c)) continue
// 		else ret.push({ s: c, i: i })
// 	}
// 	return ret
// }

export function tokenize(e){let i,s,n,u="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_$0123456789",l=[],f=0;for(;f<e.length;f++)if(s=e[i=f],u.includes(s)){for(n=s;u.includes(e[++f]);)n+=e[f];l.push({s:n,i}),f--}else if("'\"`".includes(s)){for(n=s;e[++f]!=s;)n+="\\"==e[f]?e[f++]+e[f]:e[f];l.push({s:n+e[f],i})}else if("=&|*".includes(s))l.push({s:e[f+1]==s?s+e[++f]:s,i});else{if(s.trim()=="")continue;l.push({s,i})}return l}

console.log(tokenize("console.log('Hello, World!')"))
