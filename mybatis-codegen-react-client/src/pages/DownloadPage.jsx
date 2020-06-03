import React from "react";
import {
  Flex,
  Box,
  Input,
  Text,
  Menu,
  MenuButton,
  Button,
  MenuList,
  MenuItem,
  useColorMode,
} from "@chakra-ui/core";
import Header from "../components/Header";

export default function DownloadPage() {
  const { colorMode } = useColorMode();

  return (
    <Flex
      bg={colorMode === "light" ? "gray.100" : "gray.900"}
      minHeight="100vh"
      direction="column"
      wrap="nowrap"
    >
      <Header />
      <Box
        shadow="sm"
        maxW={"90%"}
        w={["90%", "620px"]}
        mx="auto"
        mt="4rem"
        px="2rem"
        py="2rem"
        rounded="md"
        bg={colorMode === "light" ? "white" : "gray.800"}
      >
        <Text fontSize="xl">Download Zip</Text>
        <Flex mt="2rem" direction={["column", "row"]} wrap="nowrap">
          <Input
            mr={["0rem", "1rem"]}
            mb={["1rem", "0rem"]}
            flexGrow="1"
            placeholder="Database URL"
          />
          <Menu>
            <MenuButton px="2rem" as={Button} rightIcon="chevron-down">
              Select DB
            </MenuButton>
            <MenuList>
              <MenuItem>MySQL</MenuItem>
              <MenuItem>MySQL1</MenuItem>
              <MenuItem>MySQL2</MenuItem>
              <MenuItem>MySQL3</MenuItem>
            </MenuList>
          </Menu>
        </Flex>
        <Input mt="1rem" flexGrow="1" placeholder="Username" />
        <Input type="password" mt="1rem" flexGrow="1" placeholder="Password" />
        <Button
          variant="solid"
          variantColor="blue"
          mt="1rem"
          w={["100%", "auto"]}
          leftIcon="download"
        >
          Download
        </Button>
      </Box>
    </Flex>
  );
}
