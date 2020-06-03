import React from "react";
import ReactDOM from "react-dom";
import App from "./App";

import { ThemeProvider, CSSReset, ColorModeProvider } from "@chakra-ui/core";

ReactDOM.render(
  <React.StrictMode>
    <ThemeProvider>
      <ColorModeProvider>
        <CSSReset />
        <App />
      </ColorModeProvider>
    </ThemeProvider>
  </React.StrictMode>,
  document.getElementById("root")
);
