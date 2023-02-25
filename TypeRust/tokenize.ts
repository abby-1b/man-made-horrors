/*
This tokenizer makes tokens for TypeScript code.

HOWEVER, after writing it I realized it's like... pretty good. Like, it WAS
written for TypeScript, but it also works for JS, or C, or C++, or like... most
languages? Yeah, it's pretty good. I'll keep it around.

It also minifies to 512 bytes, which I'm 100% sure can be taken way lower with
a bit of tweaking.

In short, this is a good tokenizer. I like it.
*/

export interface Token {
	s: string
	i: number
}

const nameTokens = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_$0123456789"
	, repeatableOperators = "=&|*"
	, whitespace = " \n\t"
export function tokenize(str: string): Token[] {
	const ret: Token[] = []
	for (let i = 0; i < str.length; i++) {
		const si = i, c = str[i]
		if (nameTokens.includes(c)) {
			let t = c
			while (nameTokens.includes(str[++i])) t += str[i]
			ret.push({ s: t, i: si })
			i--
		} else if ("'\"`".includes(c)) {
			let t = c
			while (str[++i] != c) t += str[i] == "\\" ? str[i++] + str[i] : str[i]
			t += str[i]
			ret.push({ s: t, i: si })
		} else if (repeatableOperators.includes(c)) {
			if (str[i + 1] == c) ret.push({ s: c + str[++i], i: si })
			else ret.push({ s: c, i: si })
		} else if (whitespace.includes(c)) {
			continue
		} else {
			ret.push({ s: c, i: si })
		}
	}
	return ret
}

// const t = tokenize("console.log('Hello, World!')")
// console.log(t)
